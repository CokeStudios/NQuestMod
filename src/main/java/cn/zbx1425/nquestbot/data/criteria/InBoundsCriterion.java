package cn.zbx1425.nquestbot.data.criteria;

import cn.zbx1425.nquestbot.data.platform.PlayerStatus;
import cn.zbx1425.nquestbot.data.platform.Vec3d;

public class InBoundsCriterion implements Criterion {

    public Vec3d min;
    public Vec3d max;

    public InBoundsCriterion(Vec3d min, Vec3d max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean isFulfilled(PlayerStatus playerStatus) {
        if (playerStatus.position == null) return false;
        return playerStatus.position.isWithin(min, max);
    }
}
