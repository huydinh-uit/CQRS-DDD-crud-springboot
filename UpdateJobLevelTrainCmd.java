package vn.com.vng.mcrusprofile.command;

import java.io.Serializable;


public class UpdateJobLevelTrainCmd implements Serializable {
    private String aggId ;
    private String name;
    private String description;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
