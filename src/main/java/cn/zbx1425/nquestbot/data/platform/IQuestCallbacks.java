package cn.zbx1425.nquestbot.data.platform;

import cn.zbx1425.nquestbot.data.quest.Quest;
import cn.zbx1425.nquestbot.data.QuestEngine;
import cn.zbx1425.nquestbot.data.quest.QuestCompletionData;
import cn.zbx1425.nquestbot.data.quest.QuestProgress;

import java.util.UUID;

public interface IQuestCallbacks {

    void onQuestStarted(QuestEngine questEngine, UUID playerUuid, Quest quest);

    void onStepCompleted(QuestEngine questEngine, UUID playerUuid, Quest quest, QuestProgress progress);

    void onQuestCompleted(QuestEngine questEngine, UUID playerUuid, Quest quest, QuestCompletionData data);

}
