package vn.com.vng.mcrusprofile.dto;

import java.util.Date;

public record JobLevelTrainDto(
         Long id,
         String tenantCode,
         String code,
         String name,
         String recordStatus,
         String description,
         String updateId,
         Date updateDate

) {
}
