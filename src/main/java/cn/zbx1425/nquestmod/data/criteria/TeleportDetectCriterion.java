package cn.zbx1425.nquestmod.data.criteria;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class TeleportDetectCriterion implements Criterion {

    @Override
    public boolean isFulfilled(ServerPlayer player) {
        throw new UnsupportedOperationException("Use stateful instance");
    }

    @Override
    public Component getDisplayRepr() {
        return Component.literal("You must not warp or teleport during the quest");
    }

    @Override
    public Criterion createStatefulInstance() {
        return new Descriptor(new OverSpeedCriterion(166), getDisplayRepr());
    }
}
