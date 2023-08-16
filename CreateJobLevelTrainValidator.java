package vn.com.vng.mcrusprofile.agg.job_level_train.validator;

import com.dtsc.lib.stepbystep_validator.constraints.StepByStep;
import vn.com.vng.core.server.ApplicationContextStore;
import vn.com.vng.mcrusprofile.command.CreateJobLevelTrainCmd;
import vn.com.vng.mcrusprofile.constant.ErrorCodes;
import vn.com.vng.mcrusprofile.domain.HrtmJobLevelTrain;
import vn.com.vng.mcrusprofile.repository.HrtmJobLevelTrainDao;
import vn.com.vng.mcrusprofile.security.SecurityUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CreateJobLevelTrainValidator implements ConstraintValidator<StepByStep, CreateJobLevelTrainCmd> {

    private HrtmJobLevelTrainDao hrtmJobLevelTrainDao;

    @Override
    public void initialize(StepByStep constraintAnnotation) {
        hrtmJobLevelTrainDao = ApplicationContextStore.getApplicationContext().getBean(HrtmJobLevelTrainDao.class);
    }

    @Override
    public boolean isValid(CreateJobLevelTrainCmd value, ConstraintValidatorContext context) {
        HrtmJobLevelTrain hrtmJobLevelTrain = hrtmJobLevelTrainDao.getByCode(value.getCode(), SecurityUtils.getCurrentUserTenantCode());
        if (hrtmJobLevelTrain != null){
            context.buildConstraintViolationWithTemplate(ErrorCodes.VALIDATION_JOB_LEVEL_CODE_EXISTS)
                    .addPropertyNode("code").addConstraintViolation();
            return false;
        }
        return true;
    }
}
