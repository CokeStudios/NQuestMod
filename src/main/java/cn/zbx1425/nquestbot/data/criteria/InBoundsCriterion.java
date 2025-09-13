package cn.zbx1425.nquestbot.data.criteria;

import cn.zbx1425.nquestbot.data.platform.PlayerStatus;
import cn.zbx1425.nquestbot.data.platform.Vec3d;
import net.minecraft.network.chat.Component;

public class InBoundsCriterion implements Criterion {

    public Vec3d min;
    public Vec3d max;
    public String description;

    public InBoundsCriterion(Vec3d min, Vec3d max, String description) {
        this.min = min;
        this.max = max;
        this.description = description;
    }

    @Override
    public boolean isFulfilled(PlayerStatus playerStatus) {
        if (playerStatus.position == null) return false;
        return playerStatus.position.isWithin(min, max);
    }

    @Override
    public Component getDisplayRepr() {
        return Component.literal(description);
    }
}
