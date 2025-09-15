package cn.zbx1425.nquestbot.data.criteria;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class ManualTriggerCriterion implements Criterion {

    public String description;
    protected transient boolean isTriggered = false;

    public ManualTriggerCriterion(String description) {
        this.description = description;
    }

    @Override
    public boolean isFulfilled(ServerPlayer player) {
        return isTriggered;
    }

    @Override
    public Component getDisplayRepr() {
        return Component.literal(description).withStyle(ChatFormatting.GOLD);
    }

    @Override
    public void propagateManualTrigger() {
        isTriggered = true;
    }
}
