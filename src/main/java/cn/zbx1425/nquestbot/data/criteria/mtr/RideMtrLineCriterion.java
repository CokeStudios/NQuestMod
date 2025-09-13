package cn.zbx1425.nquestbot.data.criteria.mtr;

import cn.zbx1425.nquestbot.data.criteria.Criterion;
import cn.zbx1425.nquestbot.data.platform.PlayerStatus;

public class RideMtrLineCriterion implements Criterion {

    public String lineName;

    public RideMtrLineCriterion(String lineName) {
        this.lineName = lineName;
    }

    @Override
    public boolean isFulfilled(PlayerStatus playerStatus) {
        return lineName.equals(playerStatus.ridingTrainLine);
    }
}
