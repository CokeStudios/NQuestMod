package cn.zbx1425.nquestbot.data.quest;

import cn.zbx1425.nquestbot.data.criteria.Criterion;

import java.util.List;

public class Quest {

    public String id;
    public String name;
    public List<String> description;
    public List<Step> steps;
    public int questPoints;
}
