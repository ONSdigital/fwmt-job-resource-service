package uk.gov.ons.fwmt.resource_service.data.dto;

import lombok.Data;

import java.util.Date;

@Data
public class FieldPeriodDTO {
    Date startDate;
    Date endDate;
    String fieldPeriod;
}
