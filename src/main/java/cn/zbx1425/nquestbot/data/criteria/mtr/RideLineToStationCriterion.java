package cn.zbx1425.nquestbot.data.criteria.mtr;

import cn.zbx1425.nquestbot.data.criteria.Criterion;
import cn.zbx1425.nquestbot.data.platform.PlayerStatus;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class RideLineToStationCriterion implements Criterion {

    public String lineName;
    public String stationName;

    public RideLineToStationCriterion(String lineName, String stationName) {
        this.lineName = lineName;
        this.stationName = stationName;
    }

    @Override
    public boolean isFulfilled(PlayerStatus playerStatus) {
        if (playerStatus.containingStationAreas == null || playerStatus.ridingTrainLine == null) {
            return false;
        }
        return lineName.equals(playerStatus.ridingTrainLine) &&
               playerStatus.containingStationAreas.contains(stationName);
    }

    @Override
    public Component getDisplayRepr() {
        return Component.literal("Ride ").withStyle(ChatFormatting.GRAY)
            .append(Component.literal(lineName).withStyle(ChatFormatting.GREEN).withStyle(ChatFormatting.BOLD))
            .append(Component.literal(" to ").withStyle(ChatFormatting.GRAY))
            .append(Component.literal(stationName).withStyle(ChatFormatting.GOLD).withStyle(ChatFormatting.BOLD));
    }
}
