package cn.zbx1425.nquestbot.data.quest;

import cn.zbx1425.nquestbot.data.criteria.Criterion;

public class Step {

    public Criterion criteria;

    public Criterion createStatefulCriteria() {
        return criteria.createStatefulInstance();
    }
}
