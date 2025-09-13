package cn.zbx1425.nquestbot.data.platform;

import cn.zbx1425.nquestbot.data.quest.Quest;
import cn.zbx1425.nquestbot.data.quest.Step;
import cn.zbx1425.nquestbot.data.quest.PlayerProfile;
import cn.zbx1425.nquestbot.data.quest.QuestCompletionData;

import java.util.Collection;
import java.util.UUID;

public interface IPlatform {

    // TODO: 当玩家完成一个步骤时，由 QuestEngine 调用
    void onStepCompleted(UUID playerUuid, Step completedStep);

    // TODO: 当玩家完成一个任务时，由 QuestEngine 调用
    void onQuestCompleted(UUID playerUuid, Quest quest, QuestCompletionData data);

    // TODO: 实现玩家数据的持久化保存
    void savePlayerProfile(PlayerProfile profile);

    // TODO: 实现玩家数据的加载
    PlayerProfile loadPlayerProfile(UUID playerUuid);

    // TODO: 实现任务定义的保存
    void saveQuestDefinitions(Collection<Quest> quests);

    // TODO: 实现任务定义的加载
    Collection<Quest> loadQuestDefinitions();
}
