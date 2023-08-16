package vn.com.vng.mcrusprofile.service;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.vng.core.ag.ag_grid.request.AgGridGetRowsRequest;
import vn.com.vng.core.ag.ag_grid.request.SortModel;
import vn.com.vng.core.ag.ag_grid.response.AgGridGetRowsResponse;
import vn.com.vng.mcrusprofile.command.CreateJobLevelTrainCmd;
import vn.com.vng.mcrusprofile.command.UpdateJobLevelTrainCmd;
import vn.com.vng.mcrusprofile.command.UpdateRecordStatusJobLevelTrainCmd;
import vn.com.vng.mcrusprofile.dto.HrtmJobLevelDto;
import vn.com.vng.mcrusprofile.dto.JobLevelTrainDto;
import vn.com.vng.mcrusprofile.dto.JobLevelTrainDtoMapper;
import vn.com.vng.mcrusprofile.repository.HrtmJobLevelTrainDao;
import vn.com.vng.mcrusprofile.security.SecurityUtils;
import vn.com.vng.mcrusprofile.services.handler.CommonQH;
import vn.com.vng.mcrusprofile.util.QueryUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
    Service này sẽ send Command từ API đến CommandHandler trong Agg
*/

@Service
public class JobLevelTrainService {

    private final String TABLE_NAME = "usprofile.hrtm_job_level_train t";
    private final Class<?> clazz = JobLevelTrainDto.class;

    @Autowired
    private CommandGateway commandGateway;

    @Autowired
    private HrtmJobLevelTrainDao hrtmJobLevelTrainDao;

    @Autowired
    private final JobLevelTrainDtoMapper jobLevelTrainDtoMapper;

    @Autowired
    private CommonQH commonQH;

    public JobLevelTrainService(HrtmJobLevelTrainDao hrtmJobLevelTrainDao, JobLevelTrainDtoMapper jobLevelTrainDtoMapper) {
        this.hrtmJobLevelTrainDao = hrtmJobLevelTrainDao;
        this.jobLevelTrainDtoMapper = jobLevelTrainDtoMapper;
    }

    public JobLevelTrainDto getById(Long id){
        return jobLevelTrainDtoMapper.apply(hrtmJobLevelTrainDao.getById(id));
    }

    public JobLevelTrainDto getByAggId(String aggId){
        return jobLevelTrainDtoMapper.apply(hrtmJobLevelTrainDao.getByAggId(aggId));
    }

    public List<JobLevelTrainDto> getAllJobLevelTrain(){
        return hrtmJobLevelTrainDao.getAll()
                .stream()
                .map(jobLevelTrainDtoMapper)
                .collect(Collectors.toList());
    }

    public AgGridGetRowsResponse pivotPaging(AgGridGetRowsRequest request) {
        QueryUtil.addTenantCodeFilter(request, SecurityUtils.getCurrentUserTenantCode());
        if (request.getSortModel() == null || request.getSortModel().isEmpty()) {
            request.setSortModel(Arrays.asList(new SortModel("updateDate", "desc")));
        }

        return commonQH.pivotPaging(request, TABLE_NAME, clazz);
    }

    public String create(CreateJobLevelTrainCmd cmd) {
        return commandGateway.<String>sendAndWait(cmd);
    }

    public String update(UpdateJobLevelTrainCmd cmd) {
        return commandGateway.<String>sendAndWait(cmd);
    }

    public String disable(UpdateRecordStatusJobLevelTrainCmd cmd) {
        return commandGateway.<String>sendAndWait(cmd);
    }


}
