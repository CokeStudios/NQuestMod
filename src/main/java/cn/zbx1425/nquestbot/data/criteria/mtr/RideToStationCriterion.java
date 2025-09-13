package cn.zbx1425.nquestbot.data.criteria.mtr;

import cn.zbx1425.nquestbot.data.criteria.Criterion;
import cn.zbx1425.nquestbot.data.platform.PlayerStatus;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class RideToStationCriterion implements Criterion {

    public String stationName;

    public RideToStationCriterion(String stationName) {
        this.stationName = stationName;
    }

    @Override
    public boolean isFulfilled(PlayerStatus playerStatus) {
        if (playerStatus.containingStationAreas == null || playerStatus.ridingTrainLine == null) {
            return false;
        }
        return playerStatus.containingStationAreas.contains(stationName);
    }

    @Override
    public Component getDisplayRepr() {
        return Component.literal("Ride to ").withStyle(ChatFormatting.GRAY)
            .append(Component.literal(stationName).withStyle(ChatFormatting.GOLD).withStyle(ChatFormatting.BOLD));
    }
}
