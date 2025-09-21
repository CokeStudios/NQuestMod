package cn.zbx1425.nquestmod.data.criteria;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class Descriptor implements Criterion {

    protected Criterion base;
    public String description;

    public transient Component richDescription;

    public Descriptor(Criterion base, String description) {
        this.base = base;
        this.description = description;
        this.richDescription = Component.literal(description);
    }

    public Descriptor(Criterion base, Component richDescription) {
        this.base = base;
        this.description = richDescription.getString();
        this.richDescription = richDescription;
    }

    public Descriptor(Descriptor singleton) {
        this.base = singleton.base.createStatefulInstance();
        this.description = singleton.description;
        this.richDescription = singleton.richDescription;
    }

    @Override
    public boolean isFulfilled(ServerPlayer player) {
        return base.isFulfilled(player);
    }

    @Override
    public Component getDisplayRepr() {
        return richDescription != null ? richDescription : Component.literal(description);
    }

    @Override
    public Criterion createStatefulInstance() {
        return new Descriptor(this);
    }

    @Override
    public void propagateManualTrigger(String triggerId) {
        base.propagateManualTrigger(triggerId);
    }
}
