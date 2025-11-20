package cn.zbx1425.nquestmod.data.criteria;

import cn.zbx1425.nquestmod.interop.GenerationStatus;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class TeleportDetectCriterion implements Criterion {

    @Override
    public boolean isFulfilled(ServerPlayer player) {
        return GenerationStatus.getClientState(player).hasWarped().isSet();
    }

    @Override
    public Component getDisplayRepr() {
        return Component.literal("You must not warp or teleport during the quest");
    }
}
