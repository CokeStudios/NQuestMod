package cn.zbx1425.nquestmod.data.criteria;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class LatchingCriterion implements Criterion {

    protected Criterion base;
    protected transient boolean onceFulfilled = false;

    public LatchingCriterion(Criterion base) {
        this.base = base;
    }

    public LatchingCriterion(LatchingCriterion singleton) {
        this.base = singleton.base.createStatefulInstance();
        this.onceFulfilled = false;
    }

    @Override
    public boolean isFulfilled(ServerPlayer player) {
        if (onceFulfilled) return true;
        if (base.isFulfilled(player)) {
            onceFulfilled = true;
            return true;
        }
        return false;
    }

    @Override
    public Component getDisplayRepr() {
        return base.getDisplayRepr();
    }

    @Override
    public Criterion createStatefulInstance() {
        return new LatchingCriterion(this);
    }

    @Override
    public void propagateManualTrigger(String triggerId) {
        base.propagateManualTrigger(triggerId);
    }
}
