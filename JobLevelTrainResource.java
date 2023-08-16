package vn.com.vng.mcrusprofile.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vn.com.vng.core.ag.ag_grid.request.AgGridGetRowsRequest;
import vn.com.vng.core.ag.ag_grid.response.AgGridGetRowsResponse;
import vn.com.vng.core.rest.data.ResponseData;
import vn.com.vng.mcrusprofile.command.CreateJobLevelTrainCmd;
import vn.com.vng.mcrusprofile.command.UpdateJobLevelTrainCmd;
import vn.com.vng.mcrusprofile.command.UpdateRecordStatusJobLevelTrainCmd;
import vn.com.vng.mcrusprofile.domain.HrtmJobLevelTrain;
import vn.com.vng.mcrusprofile.dto.JobLevelTrainDto;
import vn.com.vng.mcrusprofile.service.JobLevelTrainService;

import java.util.List;

@RestController
@RequestMapping(("/api/jobLevelTrain"))
public class JobLevelTrainResource {

    @Autowired
    private JobLevelTrainService jobLevelTrainService;

    public JobLevelTrainResource(JobLevelTrainService jobLevelTrainService) {
        this.jobLevelTrainService = jobLevelTrainService;
    }

    @PostMapping(value = "/create")
    public ResponseData<String> create(@RequestBody CreateJobLevelTrainCmd cmd) {
        String aggId = jobLevelTrainService.create(cmd);
        return ResponseData.getSuccessData(aggId);
    }

    @PostMapping(value = "/update")
    public ResponseData<String> update(@RequestBody UpdateJobLevelTrainCmd cmd) {
        String aggId = jobLevelTrainService.update(cmd);
        return ResponseData.getSuccessData(aggId);
    }

    @PostMapping(value = "/updateRecordStatus")
    public  ResponseData<String> updateRecordStatus(@RequestBody UpdateRecordStatusJobLevelTrainCmd cmd){
        String aggId = jobLevelTrainService.disable(cmd);
        return ResponseData.getSuccessData(aggId);
    }

    /**
     * Raw use ?
     */
    @PostMapping(value = "/pivotPaging")
    public ResponseData<AgGridGetRowsResponse> pivotPaging(@RequestBody AgGridGetRowsRequest request) {
        AgGridGetRowsResponse rs = jobLevelTrainService.pivotPaging(request);
        return ResponseData.getSuccessData(rs);
    }

    @GetMapping(value = "/getAll")
    public List<JobLevelTrainDto> getAllJobLevelTrain(){
        return jobLevelTrainService.getAllJobLevelTrain();
    }

    @GetMapping(value = "/getById")
    public JobLevelTrainDto getById(@RequestParam Long id){
        return jobLevelTrainService.getById(id);
    }

    @GetMapping(value = "/getByAggId")
    public JobLevelTrainDto getByAggId(@RequestParam String aggId){
        return jobLevelTrainService.getByAggId(aggId);
    }

}
