package cn.zbx1425.nquestbot.data.criteria;

import net.minecraft.network.chat.Component;
import cn.zbx1425.nquestbot.data.platform.PlayerStatus;

public interface Criterion {

    boolean isFulfilled(PlayerStatus playerStatus);

    Component getDisplayRepr();
}
