package cn.zbx1425.nquestbot.data.quest;

import java.util.Map;

public class QuestCompletionData {

    public String questId;
    public long completionTime;
    public long durationMillis;
    public int questPoints;
    public Map<Integer, Long> stepDurations;
}
