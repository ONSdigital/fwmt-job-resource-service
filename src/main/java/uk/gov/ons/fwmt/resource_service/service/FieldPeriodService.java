package uk.gov.ons.fwmt.resource_service.service;

import uk.gov.ons.fwmt.resource_service.entity.FieldPeriodEntity;

import java.util.List;

public interface FieldPeriodService {
    List<FieldPeriodEntity> findFieldPeriods();
    FieldPeriodEntity createFieldPeriod(FieldPeriodEntity fieldPeriod);
    FieldPeriodEntity updateFieldPeriod(FieldPeriodEntity fieldPeriod);
    void deleteFieldPeriod(FieldPeriodEntity fieldPeriod);
}
