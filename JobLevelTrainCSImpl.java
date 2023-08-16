//package vn.com.vng.mcrusprofile.agg.job_level_train;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import vn.com.vng.mcrusprofile.domain.HrtmJobLevelTrain;
//import vn.com.vng.mcrusprofile.event.JobLevelCreatedEvent;
//import vn.com.vng.mcrusprofile.repository.JobLevelTrainDao;
//
//public class JobLevelTrainCSImpl implements JobLevelTrainCS{
//
//    @Autowired
//    private JobLevelTrainDao jobLevelTrainDao;
//    @Override
//    public void on(JobLevelCreatedEvent event) {
//        HrtmJobLevelTrain hrtmJobLevelTrain = new HrtmJobLevelTrain();
//        hrtmJobLevelTrain.setName(event.getName());
//        hrtmJobLevelTrain.setCode(event.getCode());
//        hrtmJobLevelTrain.setDescription(event.getDescription());
//        jobLevelTrainDao.persist(hrtmJobLevelTrain);
//    }
//}
