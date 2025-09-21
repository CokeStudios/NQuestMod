package cn.zbx1425.nquestmod.data.quest;

import cn.zbx1425.nquestmod.data.criteria.Criterion;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class Step implements Criterion {

    public Criterion criteria;
    public Criterion failureCriteria; // Optional

    public Step(Criterion criteria, Criterion failureCriteria) {
        this.criteria = criteria;
        this.failureCriteria = failureCriteria;
    }

    @Override
    public boolean isFulfilled(ServerPlayer player) {
        return criteria != null && criteria.isFulfilled(player);
    }

    @Override
    public Component getDisplayRepr() {
        return criteria != null ? criteria.getDisplayRepr() : Component.literal("Impossible Step");
    }

    @Override
    public Step createStatefulInstance() {
        return new Step(
            criteria != null ? criteria.createStatefulInstance() : null,
            failureCriteria != null ? failureCriteria.createStatefulInstance() : null
        );
    }

    @Override
    public void propagateManualTrigger(String triggerId) {
        if (criteria != null) criteria.propagateManualTrigger(triggerId);
        if (failureCriteria != null) failureCriteria.propagateManualTrigger(triggerId);
    }

    public Optional<Component> isFailedAndReason(Step defaultCriteria, ServerPlayer player) {
        if (failureCriteria != null) {
            if (failureCriteria.isFulfilled(player)) {
                return Optional.of(failureCriteria.getDisplayRepr());
            } else {
                return Optional.empty();
            }
        } else if (defaultCriteria != null && defaultCriteria.failureCriteria != null) {
            if (defaultCriteria.failureCriteria.isFulfilled(player)) {
                return Optional.of(defaultCriteria.failureCriteria.getDisplayRepr());
            } else {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }
}
