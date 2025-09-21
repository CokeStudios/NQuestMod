package cn.zbx1425.nquestmod.data.criteria;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class RisingEdgeAndConditionCriterion implements Criterion {

    protected final Criterion triggerCriteria;
    protected final Criterion conditionCriteria;

    protected transient boolean wasTriggerFulfilled = false;

    public RisingEdgeAndConditionCriterion(Criterion triggerCriteria, Criterion conditionCriteria) {
        this.triggerCriteria = triggerCriteria;
        this.conditionCriteria = conditionCriteria;
    }

    public RisingEdgeAndConditionCriterion(RisingEdgeAndConditionCriterion singleton) {
        this.triggerCriteria = singleton.triggerCriteria.createStatefulInstance();
        this.conditionCriteria = singleton.conditionCriteria.createStatefulInstance();
        this.wasTriggerFulfilled = false;
    }

    @Override
    public boolean isFulfilled(ServerPlayer player) {
        if (!triggerCriteria.isFulfilled(player)) {
            wasTriggerFulfilled = false;
            return false;
        }
        if (!wasTriggerFulfilled) {
            wasTriggerFulfilled = true;
            return conditionCriteria.isFulfilled(player);
        } else {
            return false;
        }
    }

    @Override
    public net.minecraft.network.chat.Component getDisplayRepr() {
        return triggerCriteria.getDisplayRepr().copy()
            .append(Component.literal(" while: ").withStyle(ChatFormatting.GRAY))
            .append(conditionCriteria.getDisplayRepr());
    }

    @Override
    public void propagateManualTrigger(String triggerId) {
        triggerCriteria.propagateManualTrigger(triggerId);
        conditionCriteria.propagateManualTrigger(triggerId);
    }

    @Override
    public Criterion createStatefulInstance() {
        return new RisingEdgeAndConditionCriterion(this);
    }
}
