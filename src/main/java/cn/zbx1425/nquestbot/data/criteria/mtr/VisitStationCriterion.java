package cn.zbx1425.nquestbot.data.criteria.mtr;

import cn.zbx1425.nquestbot.data.criteria.Criterion;
import cn.zbx1425.nquestbot.data.platform.PlayerStatus;

public class VisitStationCriterion implements Criterion {

    public String stationName;

    public VisitStationCriterion(String stationName) {
        this.stationName = stationName;
    }

    @Override
    public boolean isFulfilled(PlayerStatus playerStatus) {
        if (playerStatus.containingStationAreas == null) return false;
        return playerStatus.containingStationAreas.contains(stationName);
    }
}
