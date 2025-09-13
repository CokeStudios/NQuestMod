package cn.zbx1425.nquestbot.data.criteria.mtr;

import cn.zbx1425.nquestbot.data.criteria.Criterion;
import cn.zbx1425.nquestbot.data.platform.PlayerStatus;

public class RideLineToStationCriterion implements Criterion {

    public String lineName;
    public String stationName;

    public RideLineToStationCriterion(String lineName, String stationName) {
        this.lineName = lineName;
        this.stationName = stationName;
    }

    @Override
    public boolean isFulfilled(PlayerStatus playerStatus) {
        if (playerStatus.containingStationAreas == null || playerStatus.ridingTrainLine == null) {
            return false;
        }
        return lineName.equals(playerStatus.ridingTrainLine) &&
               playerStatus.containingStationAreas.contains(stationName);
    }
}
