package cn.zbx1425.nquestbot.data.leaderboard;

import java.util.UUID;

public class PlayerQPScore {

    public final UUID playerUuid;
    public final int questPoints;

    public PlayerQPScore(UUID playerUuid, int questPoints) {
        this.playerUuid = playerUuid;
        this.questPoints = questPoints;
    }
}
