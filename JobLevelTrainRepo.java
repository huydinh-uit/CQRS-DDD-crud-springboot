package vn.com.vng.mcrusprofile.agg.job_level_train;

import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.modelling.command.GenericJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository(JobLevelTrainRepo.BEAN_NAME)
public class JobLevelTrainRepo extends GenericJpaRepository<JobLevelTrainAgg> {

	public static final String BEAN_NAME = "vn.com.vng.mcrusprofile.agg.job_level_train.JobLevelTrainRepo";

    /**
     * @param entityManagerProvider
     * @param aggregateType
     * @param eventBus
     */
    @Autowired
    public JobLevelTrainRepo(
            EntityManagerProvider entityManagerProvider,
            @Value(value = "vn.com.vng.mcrusprofile.agg.job_level_train.JobLevelTrainAgg")
            Class<JobLevelTrainAgg> aggregateType,
            EventBus eventBus) {
    	super(builder(aggregateType).entityManagerProvider(entityManagerProvider).eventBus(eventBus));
    }
}
