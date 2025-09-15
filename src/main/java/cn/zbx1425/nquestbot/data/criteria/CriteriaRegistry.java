package cn.zbx1425.nquestbot.data.criteria;

import cn.zbx1425.nquestbot.data.RuntimeTypeAdapterFactory;
import cn.zbx1425.nquestbot.data.criteria.mtr.RideLineToStationCriterion;
import cn.zbx1425.nquestbot.data.criteria.mtr.RideLineCriterion;
import cn.zbx1425.nquestbot.data.criteria.mtr.RideToStationCriterion;
import cn.zbx1425.nquestbot.data.criteria.mtr.VisitStationCriterion;
import com.google.gson.GsonBuilder;

public class CriteriaRegistry {

    public static RuntimeTypeAdapterFactory<Criterion> getFactory() {
        return RuntimeTypeAdapterFactory.of(Criterion.class, "type")
                .registerSubtype(ManualTriggerCriterion.class)
                .registerSubtype(InBoundsCriterion.class)
                .registerSubtype(RideLineToStationCriterion.class)
                .registerSubtype(RideLineCriterion.class)
                .registerSubtype(RideToStationCriterion.class)
                .registerSubtype(VisitStationCriterion.class)

                .registerSubtype(AndCriterion.class)
                .registerSubtype(LatchingCriterion.class)
                .registerSubtype(RisingEdgeAndConditionCriterion.class)
                ;
    }
}
