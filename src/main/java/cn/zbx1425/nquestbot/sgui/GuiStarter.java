package cn.zbx1425.nquestbot.sgui;

import cn.zbx1425.nquestbot.NQuestBot;
import cn.zbx1425.nquestbot.data.quest.PlayerProfile;
import net.minecraft.server.level.ServerPlayer;

public class GuiStarter {

    public static void openEntry(ServerPlayer player) {
        PlayerProfile profile = NQuestBot.INSTANCE.questDispatcher.playerProfiles.get(player.getGameProfile().getId());
        if (profile != null && !profile.activeQuests.isEmpty()) {
            new CurrentQuestScreen(player, new MainMenuScreen(player)).open();
        } else {
            new MainMenuScreen(player).open();
        }
    }
}
