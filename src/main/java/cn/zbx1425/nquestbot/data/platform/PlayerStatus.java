package cn.zbx1425.nquestbot.data.platform;

import net.minecraft.server.level.ServerPlayer;

import java.util.Set;
import java.util.UUID;

public class PlayerStatus {

    public final UUID playerUuid;
    public final Vec3d position;
    public final String ridingTrainLine; // 可为 null
    public final Set<String> containingStationAreas;

    public PlayerStatus(UUID playerUuid, Vec3d position, String ridingTrainLine, Set<String> containingStationAreas) {
        this.playerUuid = playerUuid;
        this.position = position;
        this.ridingTrainLine = ridingTrainLine;
        this.containingStationAreas = containingStationAreas;
    }

    public static PlayerStatus fromPlayer(ServerPlayer player) {
        UUID playerUuid = player.getUUID();
        Vec3d position = new Vec3d(player.getX(), player.getY(), player.getZ());
        String ridingTrainLine = null;
        Set<String> containingStationAreas = Set.of();
        return new PlayerStatus(playerUuid, position, ridingTrainLine, containingStationAreas);
    }
}
