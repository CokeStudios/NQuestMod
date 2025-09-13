package cn.zbx1425.nquestbot;

import cn.zbx1425.nquestbot.data.QuestEngine;
import cn.zbx1425.nquestbot.data.QuestPersistence;
import cn.zbx1425.nquestbot.data.platform.PlayerStatus;
import cn.zbx1425.nquestbot.data.quest.PlayerProfile;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.LevelResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class NQuestBot implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("NQuestBot");
    public QuestPersistence questStorage;
    public QuestEngine questEngine;
    public QuestNotifications questNotifications;

    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTING.register((server) -> {
            try {
                Path basePath = server.getWorldPath(LevelResource.ROOT).resolve("quest_bot");
                Files.createDirectories(basePath);
                // Well this isn't clean but we only have one server instance anyway
                questStorage = new QuestPersistence(basePath);
                questNotifications = new QuestNotifications(server);
                questEngine = new QuestEngine(questNotifications);
            } catch (IOException ex) {
                LOGGER.error("Failed to initialize quest storage", ex);
            }
        });

        ServerPlayConnectionEvents.JOIN.register((packetListener, packetSender, server) -> {
            ServerPlayer player = packetListener.getPlayer();
            try {
                questEngine.playerProfiles.put(player.getGameProfile().getId(),
                        questStorage.loadPlayerProfile(player.getGameProfile().getId()));
                questNotifications.onPlayerJoin(questEngine, player.getGameProfile().getId());
            } catch (IOException ex) {
                LOGGER.error("Failed to load player profile for {}", player.getGameProfile().getName(), ex);
            }
        });

        ServerPlayConnectionEvents.DISCONNECT.register((packetListener, server) -> {
            ServerPlayer player = packetListener.getPlayer();
            PlayerProfile profile = questEngine.playerProfiles.remove(player.getGameProfile().getId());
            if (profile != null) {
                try {
                    questStorage.savePlayerProfile(profile);
                } catch (IOException ex) {
                    LOGGER.error("Failed to save player profile for {}", player.getGameProfile().getName(), ex);
                }
            }
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            assert questEngine != null;
            if (server.getTickCount() % 20 != 15) return; // Every second
            questEngine.updatePlayers(playerUuid -> {
                ServerPlayer player = server.getPlayerList().getPlayer(playerUuid);
                if (player != null) {
                    return PlayerStatus.fromPlayer(player);
                }
                return null; // Player not online
            });
        });
    }
}
