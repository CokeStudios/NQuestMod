package cn.zbx1425.nquestbot.data.quest;

import cn.zbx1425.nquestbot.data.criteria.Criterion;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.List;
import java.util.UUID;

public class Step {

    public UUID id;
    public List<Criterion> criteria;
    public boolean needsManualTrigger;
    public String manualTriggerDescription;

    public Component getDisplayRepr() {
        boolean first = true;
        MutableComponent objectives = Component.literal("").copy();
        for (Criterion criterion : criteria) {
            if (!first) {
                objectives.append(Component.literal(" and ").withStyle(ChatFormatting.GRAY));
            }
            objectives.append(criterion.getDisplayRepr());
            first = false;
        }
        if (needsManualTrigger) {
            if (!first) {
                objectives.append(Component.literal(" and ").withStyle(ChatFormatting.GRAY));
            }
            objectives.append(Component.literal(manualTriggerDescription));
        }
        return objectives;
    }
}
