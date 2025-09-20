package cn.zbx1425.nquestbot.sgui;

import cn.zbx1425.nquestbot.NQuestBot;
import cn.zbx1425.nquestbot.data.quest.Quest;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.BaseSlotGui;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Items;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class QuestListScreen extends ItemListGui<Quest> {

    private final Consumer<Quest> callback;

    public QuestListScreen(ServerPlayer player, BaseSlotGui parent, Consumer<Quest> callback) {
        super(MenuType.GENERIC_9x4, player, parent);
        this.callback = callback;
        setTitle(Component.literal("Select a Quest"));
        init();
    }

    @Override
    protected CompletableFuture<Pair<List<Quest>, Integer>> supplyItems(int offset, int limit) {
        List<Quest> allQuests = new ArrayList<>(NQuestBot.INSTANCE.questDispatcher.quests.values());
        int totalSize = allQuests.size();
        List<Quest> pageQuests = allQuests.stream().skip(offset).limit(limit).collect(Collectors.toList());
        return CompletableFuture.completedFuture(Pair.of(pageQuests, totalSize));
    }

    @Override
    protected GuiElementBuilder createElementForItem(Quest item, int index) {
        return new GuiElementBuilder(Items.BOOK)
                .setName(Component.literal(item.name))
                .setLore(formatDescription(item.description))
                .setCallback((i, t, a) -> this.callback.accept(item));
    }

    private List<Component> formatDescription(String description) {
        return List.of(description.split("\n")).stream()
                .map(line -> Component.literal(line).withStyle(ChatFormatting.GRAY))
                .collect(Collectors.toList());
    }
}
