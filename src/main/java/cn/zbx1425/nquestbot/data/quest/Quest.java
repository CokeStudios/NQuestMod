package cn.zbx1425.nquestbot.data.quest;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.stream.Collectors;

public class Quest {

    public String id;
    public String name;
    public String description;
    public List<Step> steps;
    public int questPoints;

    public List<Component> formatDescription() {
        return List.of(description.split("\n")).stream()
            .map(line -> Component.literal(line).withStyle(ChatFormatting.GRAY))
            .collect(Collectors.toList());
    }
}
