package cn.zbx1425.nquestbot.data.criteria;

import cn.zbx1425.nquestbot.data.platform.PlayerStatus;

public interface Criterion {

    boolean isFulfilled(PlayerStatus playerStatus);
}
