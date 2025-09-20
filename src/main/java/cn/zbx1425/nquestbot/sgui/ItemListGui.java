package cn.zbx1425.nquestbot.sgui;

import cn.zbx1425.nquestbot.NQuestBot;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.BaseSlotGui;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Items;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class ItemListGui<TItem> extends ParentedGui {

    protected int rowContentStarts;
    protected int rowContentEnds;

    protected int page = 0;

    public ItemListGui(MenuType<?> type, ServerPlayer player, BaseSlotGui parent) {
        super(type, player, parent);
        rowContentStarts = 0;
        rowContentEnds = height - 2;
    }

    @Override
    public void init() {
        super.init();

        int pageSize = (rowContentEnds - rowContentStarts + 1) * 9;
        int offset = page * pageSize;
        CompletableFuture<Pair<List<TItem>, Integer>> pageItemsFuture = supplyItems(offset, pageSize);
        if (!pageItemsFuture.isDone()) {
            for (int slot = rowContentStarts * 9; slot < (rowContentEnds + 1) * 9; slot++) {
                clearSlot(slot);
            }
            setSlot((int)Math.ceil((rowContentStarts + rowContentEnds) / 2.0) * 9 + 4, new GuiElementBuilder(Items.SNOWBALL)
                .setName(Component.literal("Loading...")));
        }
        pageItemsFuture.thenAccept(pageItemsAndSize -> getPlayer().getServer().execute(() -> {
            List<TItem> pageItems = pageItemsAndSize.getLeft();
            int totalSize = pageItemsAndSize.getRight();
            for (int slot = rowContentStarts * 9; slot < (rowContentEnds + 1) * 9; slot++) {
                int index = slot - rowContentStarts * 9 + offset;
                if (index < pageItems.size()) {
                    TItem item = pageItems.get(index);
                    setSlot(slot, createElementForItem(item, index));
                } else {
                    clearSlot(slot);
                }
            }
            if (page > 0) {
                setSlot(9 * (rowContentEnds + 1) + 5, new GuiElementBuilder(Items.PAPER)
                    .setName(Component.literal("<<<<"))
                    .setCount(page)
                    .setCallback((index, type, action) -> {
                        page--;
                        init();
                    })
                );
            } else {
                clearSlot(9 * (rowContentEnds + 1) + 5);
            }
            if ((page + 1) * pageSize < totalSize) {
                setSlot(9 * (rowContentEnds + 1) + 7, new GuiElementBuilder(Items.PAPER)
                    .setName(Component.literal(">>>>"))
                    .setCount(page + 2)
                    .setCallback((index, type, action) -> {
                        page++;
                        init();
                    })
                );
            } else {
                clearSlot(9 * (rowContentEnds + 1) + 7);
            }
            if (totalSize > pageSize) {
                Component pageInfo = totalSize == 99999
                    ? Component.literal("Page " + (page + 1))
                    : Component.literal("Page " + (page + 1) + " of " + ((totalSize + pageSize - 1) / pageSize));
                setSlot(9 * (rowContentEnds + 1) + 6, new GuiElementBuilder(Items.CHAIN)
                    .setCount(page + 1)
                    .setName(pageInfo)
                );
            } else {
                clearSlot(9 * (rowContentEnds + 1) + 6);
            }
        })).exceptionally(ex -> {
            NQuestBot.LOGGER.error("Error loading items for ItemListGui", ex);
            for (int slot = rowContentStarts * 9; slot < (rowContentEnds + 1) * 9; slot++) {
                clearSlot(slot);
            }
            setSlot((int)Math.ceil((rowContentStarts + rowContentEnds) / 2.0) * 9 + 4, new GuiElementBuilder(Items.BARRIER)
                .setName(Component.literal("Error loading items"))
                .setLore(List.of(Component.literal(ex.getMessage())))
            );
            return null;
        });
    }

    protected abstract CompletableFuture<Pair<List<TItem>, Integer>> supplyItems(int offset, int limit);

    protected abstract GuiElementBuilder createElementForItem(TItem item, int index);
}
