package cn.zbx1425.nquestbot.data.platform;

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
}
