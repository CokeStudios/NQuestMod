package cn.zbx1425.nquestmod.interop;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMaps;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class GenerationStatus {

    public static long currentGeneration = -1;

    public static final Object2ObjectMap<UUID, ClientState> CLIENTS = Object2ObjectMaps.synchronize(new Object2ObjectOpenHashMap<>());

    public static ClientState getClientState(ServerPlayer player) {
        return CLIENTS.computeIfAbsent(player.getGameProfile().getId(), k -> new ClientState());
    }

    public static void nextGeneration() {
        currentGeneration = System.nanoTime();
    }

    public record ClientState(GenerationLatch hasWarped) {

        public ClientState() {
            this(new GenerationLatch());
        }
    }

    public static class GenerationLatch {

        private long setTime = -1;

        public void set() {
            setTime = System.nanoTime();
        }

        public void reset() {
            setTime = -1;
        }

        public boolean isSet() {
            return setTime > currentGeneration;
        }
    }
}
