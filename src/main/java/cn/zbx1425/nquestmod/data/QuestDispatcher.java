package cn.zbx1425.nquestmod.data;

import cn.zbx1425.nquestmod.data.ranking.QuestUserDatabase;
import cn.zbx1425.nquestmod.data.quest.Quest;
import cn.zbx1425.nquestmod.data.quest.Step;
import cn.zbx1425.nquestmod.data.quest.PlayerProfile;
import cn.zbx1425.nquestmod.data.quest.QuestCompletionData;
import cn.zbx1425.nquestmod.data.quest.QuestProgress;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.sql.SQLException;
import java.util.*;
import java.util.List;
import java.util.function.Function;

public class QuestDispatcher {

    private final IQuestCallbacks callback;
    private final QuestUserDatabase databaseManager;
    public Map<String, Quest> quests;
    public final Map<UUID, PlayerProfile> playerProfiles = new HashMap<>();

    public QuestDispatcher(IQuestCallbacks callback, QuestUserDatabase databaseManager) {
        this.callback = callback;
        this.databaseManager = databaseManager;
        this.quests = Map.of();
    }

    public PlayerProfile getPlayerProfile(UUID playerUuid) {
        return playerProfiles.get(playerUuid);
    }

    public boolean updatePlayers(Function<UUID, ServerPlayer> playerGetter) {
        boolean isAnyQuestGoingOn = false;
        for (PlayerProfile profile : playerProfiles.values()) {
            if (profile.activeQuests.isEmpty()) {
                continue;
            }

            // Iterate over a copy to avoid ConcurrentModificationException
            for (QuestProgress progress : new ArrayList<>(profile.activeQuests.values())) {
                Quest quest = quests.get(progress.questId);
                if (quest == null || progress.currentStepIndex >= quest.steps.size()) {
                    continue;
                }
                isAnyQuestGoingOn = true;

                ServerPlayer player = playerGetter.apply(profile.playerUuid);
                if (player == null) continue; // Player might not be online, but has active quest

                tryAdvance(profile, quest, progress, player, null);
            }
        }
        return isAnyQuestGoingOn;
    }

    public void triggerManualCriterion(UUID playerUuid, String triggerId, ServerPlayer player) throws QuestException {
        PlayerProfile profile = playerProfiles.get(playerUuid);
        if (profile == null) throw new QuestException(QuestException.Type.PLAYER_NOT_FOUND);

        for (QuestProgress progress : new ArrayList<>(profile.activeQuests.values())) {
            Quest quest = quests.get(progress.questId);
            if (quest == null || progress.currentStepIndex >= quest.steps.size()) {
                continue;
            }
            tryAdvance(profile, quest, progress, player, triggerId);
        }
    }

    public void startQuest(UUID playerUuid, String questId) throws QuestException {
        PlayerProfile profile = playerProfiles.get(playerUuid);
        if (profile == null) throw new QuestException(QuestException.Type.PLAYER_NOT_FOUND);
        Quest quest = quests.get(questId);
        if (quest == null) throw new QuestException(QuestException.Type.QUEST_NOT_FOUND);
        if (profile.activeQuests.containsKey(questId)) throw new QuestException(QuestException.Type.QUEST_ALREADY_STARTED);
        if (!profile.activeQuests.isEmpty()) throw new QuestException(QuestException.Type.QUEST_ONLY_ONE_AT_A_TIME);

        QuestProgress progress = new QuestProgress();
        progress.questId = questId;
        progress.currentStepIndex = 0;
        progress.questStartTime = System.currentTimeMillis();
        progress.stepStartTimes = new HashMap<>();
        progress.stepStartTimes.put(0, progress.questStartTime);

        profile.activeQuests.put(questId, progress);
        callback.onQuestStarted(this, playerUuid, quest);
    }

    public void stopQuests(UUID playerUuid) throws QuestException {
        PlayerProfile profile = playerProfiles.get(playerUuid);
        if (profile == null) throw new QuestException(QuestException.Type.PLAYER_NOT_FOUND);
        if (profile.activeQuests.isEmpty()) throw new QuestException(QuestException.Type.QUEST_NOT_STARTED);
        List<QuestProgress> progresses = new ArrayList<>(profile.activeQuests.values());
        profile.activeQuests.clear();
        for (QuestProgress progress : progresses) {
            Quest quest = quests.get(progress.questId);
            if (quest != null) {
                callback.onQuestAborted(this, playerUuid, quest);
            }
        }
    }

    private void advanceQuestStep(PlayerProfile profile, QuestProgress progress, Quest quest) {
        // Mark current step as complete
        long now = System.currentTimeMillis();
        progress.currentStepIndex++;
        callback.onStepCompleted(this, profile.playerUuid, quest, progress);

        if (progress.currentStepIndex >= quest.steps.size()) {
            // Quest completed
            profile.activeQuests.remove(progress.questId);

            QuestCompletionData completionData = new QuestCompletionData();
            completionData.playerUuid = profile.playerUuid;
            completionData.questId = quest.id;
            completionData.completionTime = now;
            completionData.durationMillis = now - progress.questStartTime;
            completionData.questPoints = quest.questPoints;
            
            // Calculate and store step durations
            completionData.stepDurations = new HashMap<>();
            long lastTimestamp = progress.questStartTime;
            for (int i = 0; i < quest.steps.size(); i++) {
                long stepEndTimestamp = progress.stepStartTimes.getOrDefault(i + 1, now);
                long stepStartTimestamp = progress.stepStartTimes.getOrDefault(i, lastTimestamp);
                completionData.stepDurations.put(i, stepEndTimestamp - stepStartTimestamp);
                lastTimestamp = stepEndTimestamp;
            }

            profile.totalQuestPoints += quest.questPoints;
            profile.totalQuestCompletions += 1;

            callback.onQuestCompleted(this, profile.playerUuid, quest, completionData);
            try {
                databaseManager.addQuestCompletion(profile.playerUuid, quest, completionData);
            } catch (SQLException e) {
                // Let's hope this doesn't happen.
                throw new RuntimeException(e);
            }
        } else {
            // Advance to next step
            progress.stepStartTimes.put(progress.currentStepIndex, now);
        }
    }

    private void tryAdvance(PlayerProfile profile, Quest quest, QuestProgress progress, ServerPlayer player, String triggerId) {
        if (progress.currentStepStateful == null) {
            Step currentStep = quest.steps.get(progress.currentStepIndex);
            progress.currentStepStateful = currentStep.createStatefulInstance();
            progress.defaultCriteriaStateful = quest.defaultCriteria == null ? null : quest.defaultCriteria.createStatefulInstance();
        }

        if (triggerId != null) {
            progress.currentStepStateful.propagateManualTrigger(triggerId);
            if (progress.defaultCriteriaStateful != null) {
                progress.defaultCriteriaStateful.propagateManualTrigger(triggerId);
            }
        }

        Optional<Component> questFailedAndReason = progress.currentStepStateful.isFailedAndReason(progress.defaultCriteriaStateful, player);
        if (questFailedAndReason.isPresent()) {
            profile.activeQuests.remove(progress.questId);
            callback.onQuestFailed(this, profile.playerUuid, quest, questFailedAndReason.get());
            return;
        }

        if (progress.currentStepStateful.isFulfilled(player)) {
            progress.currentStepStateful = null; // Reset for next step
            progress.defaultCriteriaStateful = null;
            advanceQuestStep(profile, progress, quest);
        }
    }
}
