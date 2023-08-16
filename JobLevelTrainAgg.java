package vn.com.vng.mcrusprofile.agg.job_level_train;


import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import vn.com.vng.core.server.axon.AnnotatedAggregateRoot;
import vn.com.vng.core.server.hibernate_validator.CacheObject;
import vn.com.vng.mcrusprofile.agg.job_level.JobLevelAgg;
import vn.com.vng.mcrusprofile.agg.unit.UnitRepo;
import vn.com.vng.mcrusprofile.command.CreateJobLevelTrainCmd;
import vn.com.vng.mcrusprofile.command.UpdateJobLevelTrainCmd;
import vn.com.vng.mcrusprofile.domain.HrtmJobLevel;
import vn.com.vng.mcrusprofile.domain.HrtmJobLevelTrain;
import vn.com.vng.mcrusprofile.domain.HrtmUnit;
import vn.com.vng.mcrusprofile.event.JobLevelCreatedEvent;
import vn.com.vng.mcrusprofile.event.JobLevelUpdatedEvent;
import vn.com.vng.mcrusprofile.repository.HrtmJobLevelTrainDao;
import vn.com.vng.mcrusprofile.security.SecurityUtils;
import vn.com.vng.mcrusprofile.util.MetaDataUtils;
import vn.com.vng.security.fieldsec.fsm.utils.TextUtils;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.UUID;


@Aggregate(repository = JobLevelTrainRepo.BEAN_NAME)
@Entity
@Table(name = "agg_hrtm_job_level_train")
@Configurable(autowire = Autowire.BY_TYPE, preConstruction = true, dependencyCheck = true)
public class JobLevelTrainAgg extends AnnotatedAggregateRoot {

    //Question?
    public static final String CACHE_JOBLEVELTRAIN = "cacheJobLevelTrain";

    @Transient
    @Autowired
    private HrtmJobLevelTrainDao hrtmJobLevelTrainDao;

    public JobLevelTrainAgg() {
    }

    /**
     * Question? Flow của AXON
     * CommandHandler(CH) tạo event
     * CH này tạo JobLevelCreatedEvent
     */
    @CommandHandler
    public JobLevelTrainAgg(CreateJobLevelTrainCmd cmd) {
        AggregateLifecycle.apply(new JobLevelCreatedEvent(
                cmd.getAggId(),
                cmd.getName(),
                cmd.getCode(),
                cmd.getDescription()
        ));
    }

    /**
     * Xử lí Event vừa tạo
     */
    @EventSourcingHandler
    public void on(JobLevelCreatedEvent event) {
        this.aggId = UUID.randomUUID().toString();

        HrtmJobLevelTrain hrtmJobLevelTrain = new HrtmJobLevelTrain();
        hrtmJobLevelTrain.setTenantCode(SecurityUtils.getCurrentUserTenantCode());

        MetaDataUtils.forCreate(hrtmJobLevelTrain);

        hrtmJobLevelTrain.setAggId(aggId);
        hrtmJobLevelTrain.setName(event.getName());
        hrtmJobLevelTrain.setCode(event.getCode());
        hrtmJobLevelTrain.setDescription(event.getDescription());
        hrtmJobLevelTrainDao.persist(hrtmJobLevelTrain);
    }
}
    //CH này tạo JobLevelUpdatedEvent
////    @CommandHandler
//    public JobLevelTrainAgg(UpdateJobLevelTrainCmd cmd) {
//        on(new JobLevelUpdatedEvent(
//                cmd.getAggId(),
//                cmd.getName(),
//                cmd.getDescription()
//        ));
////        AggregateLifecycle.apply(new JobLevelUpdatedEvent(
////                cmd.getAggId(),
////                cmd.getName(),
////                cmd.getDescription()
////        ));
//    }
////    @EventSourcingHandler
//    public void on(JobLevelUpdatedEvent event) {
//        HrtmJobLevelTrain hrtmJobLevelTrain = hrtmJobLevelTrainDao.getByAggId(event.getId());
//        hrtmJobLevelTrain.setName(event.getName());
//        hrtmJobLevelTrain.setDescription(event.getDescription());
//        hrtmJobLevelTrainDao.save(hrtmJobLevelTrain);
//    }
//
//}
//
////    @EventSourcingHandler
////    public void on(JobLevelCreatedEvent event) throws Exception{
////            this.aggId = event.getId();
////            this.code = event.getCode();
////            this.name = event.getName();
////            this.description = event.getDescription();
////
////    }






