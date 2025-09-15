package cn.zbx1425.nquestbot.data.quest;

import cn.zbx1425.nquestbot.data.criteria.Criterion;

import java.util.Map;

public class QuestProgress {

    public String questId;
    public int currentStepIndex;
    public long questStartTime;
    public Map<Integer, Long> stepStartTimes;

    public transient Criterion currentStepStatefulCriteria;
}
