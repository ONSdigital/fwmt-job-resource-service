package uk.gov.ons.fwmt.resource_service.data.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FieldPeriodDTO {
    LocalDate startDate;
    LocalDate endDate;
    String fieldPeriod;
}
