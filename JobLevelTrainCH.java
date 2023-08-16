package vn.com.vng.mcrusprofile.agg.job_level_train;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import vn.com.vng.mcrusprofile.command.UpdateJobLevelTrainCmd;
import vn.com.vng.mcrusprofile.command.UpdateRecordStatusJobLevelTrainCmd;
import vn.com.vng.mcrusprofile.domain.HrtmJobLevelTrain;

import vn.com.vng.mcrusprofile.event.JobLevelUpdatedEvent;
import vn.com.vng.mcrusprofile.event.UpdateRecordStatusJobLevelTrainEvent;
import vn.com.vng.mcrusprofile.repository.HrtmJobLevelTrainDao;



@Service
public class JobLevelTrainCH {

    @Autowired
    @Qualifier(JobLevelTrainRepo.BEAN_NAME)
    private JobLevelTrainRepo repo;

    @Autowired
    private HrtmJobLevelTrainDao hrtmJobLevelTrainDao;
    @CommandHandler
    public void JobLevelTrainAgg(UpdateJobLevelTrainCmd cmd) {
        on(new JobLevelUpdatedEvent(
                cmd.getAggId(),
                cmd.getName(),
                cmd.getDescription()
        ));
//        AggregateLifecycle.apply(new JobLevelUpdatedEvent(
//                cmd.getAggId(),
//                cmd.getName(),
//                cmd.getDescription()
//        ));
    }
    @EventSourcingHandler
    public void on(JobLevelUpdatedEvent event) {
        HrtmJobLevelTrain hrtmJobLevelTrain = hrtmJobLevelTrainDao.getByAggId(event.getId());
        hrtmJobLevelTrain.setName(event.getName());
        hrtmJobLevelTrain.setDescription(event.getDescription());
        hrtmJobLevelTrainDao.save(hrtmJobLevelTrain);
    }

    /**
     * Disable job level
    */
    @CommandHandler
    public void JobLevelTrainAgg(UpdateRecordStatusJobLevelTrainCmd cmd) {
        on(new UpdateRecordStatusJobLevelTrainEvent(
                cmd.getAggId(),
                cmd.getRecordStatus()
        ));
//        AggregateLifecycle.apply(new JobLevelUpdatedEvent(
//                cmd.getAggId(),
//                cmd.getName(),
//                cmd.getDescription()
//        ));
    }
    @EventSourcingHandler
    public void on(UpdateRecordStatusJobLevelTrainEvent event) {
        HrtmJobLevelTrain hrtmJobLevelTrain = hrtmJobLevelTrainDao.getByAggId(event.getAggId());
        hrtmJobLevelTrain.setRecordStatus(event.getRecordStatus());
        hrtmJobLevelTrainDao.save(hrtmJobLevelTrain);
    }
}
