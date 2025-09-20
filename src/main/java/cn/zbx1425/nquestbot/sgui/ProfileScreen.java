package cn.zbx1425.nquestbot.sgui;

import cn.zbx1425.nquestbot.NQuestBot;
import cn.zbx1425.nquestbot.data.quest.PlayerProfile;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.BaseSlotGui;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Items;

import java.util.List;

public class ProfileScreen extends ParentedGui {

    public ProfileScreen(ServerPlayer player, BaseSlotGui parent) {
        super(MenuType.GENERIC_9x3, player, parent);
        setTitle(Component.literal("My Profile"));
        init();
    }

    @Override
    public void init() {
        super.init();

        PlayerProfile profile = NQuestBot.INSTANCE.questDispatcher.playerProfiles.get(player.getGameProfile().getId());
        if (profile == null) return;

        // Player Head
        setSlot(4, new GuiElementBuilder(Items.PLAYER_HEAD)
                .setSkullOwner(player.getGameProfile(), player.getServer())
                .setName(Component.literal(player.getGameProfile().getName()))
        );

        // Total Quest Points
        setSlot(11, new GuiElementBuilder(Items.EXPERIENCE_BOTTLE)
                .setName(Component.literal("Total Quest Points"))
                .setLore(List.of(Component.literal(String.valueOf(profile.totalQuestPoints))))
        );

        // Total Quests Completed
        setSlot(13, new GuiElementBuilder(Items.EMERALD)
                .setName(Component.literal("Total Quests Completed"))
                .setLore(List.of(Component.literal(String.valueOf(profile.totalQuestCompletions))))
        );

        // Quest History
        setSlot(15, new GuiElementBuilder(Items.CLOCK)
                .setName(Component.literal("Quest History"))
                .setLore(List.of(Component.literal("View your past quest completions.")))
                .setCallback((index, type, action) -> {
                    new QuestHistoryScreen(player, this).open();
                })
        );
    }
}
