package cn.zbx1425.nquestbot.data.criteria.mtr;

import cn.zbx1425.nquestbot.data.criteria.Criterion;
import cn.zbx1425.nquestbot.data.platform.PlayerStatus;

public class RideToStationCriterion implements Criterion {

    public String stationName;

    public RideToStationCriterion(String stationName) {
        this.stationName = stationName;
    }

    @Override
    public boolean isFulfilled(PlayerStatus playerStatus) {
        if (playerStatus.containingStationAreas == null || playerStatus.ridingTrainLine == null) {
            return false;
        }
        return playerStatus.containingStationAreas.contains(stationName);
    }
}
