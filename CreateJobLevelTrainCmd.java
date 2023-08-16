package vn.com.vng.mcrusprofile.command;



import com.dtsc.lib.stepbystep_validator.constraints.StepByStep;
import com.dtsc.lib.stepbystep_validator.constraints.Validator;
import com.dtsc.lib.stepbystep_validator.constraints.group.GroupOrder;
import com.dtsc.lib.stepbystep_validator.constraints.group.Level1;
import com.dtsc.lib.stepbystep_validator.constraints.group.Level9;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
@GroupOrder
@StepByStep(value = {
        @Validator(name = "vn.com.vng.mcrusprofile.agg." +
                "job_level_train.validator.CreateJobLevelTrainValidator") },
        groups = Level9.class)

public class CreateJobLevelTrainCmd implements Serializable {

    private String aggId ;
    private String name;
    @NotEmpty(groups = Level1.class)
    private String code;
    private String description;

    public CreateJobLevelTrainCmd() { super();    }

    public CreateJobLevelTrainCmd(String aggId, String name, String code, String description) {
        this.aggId = aggId;
        this.name = name;
        this.code = code;
        this.description = description;
    }



    public String getAggId() {
        return aggId;
    }
    public void setAggId(String aggId) {
        this.aggId = aggId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
