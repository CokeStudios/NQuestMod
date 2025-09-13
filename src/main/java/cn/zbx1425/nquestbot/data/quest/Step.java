package cn.zbx1425.nquestbot.data.quest;

import cn.zbx1425.nquestbot.data.criteria.Criterion;

import java.util.List;

public class Step {

    public String id;
    public String name;
    public List<Criterion> criteria;
    public boolean needsManualTrigger;
}
