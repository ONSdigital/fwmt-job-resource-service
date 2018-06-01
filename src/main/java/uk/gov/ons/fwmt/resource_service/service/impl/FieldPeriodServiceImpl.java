package uk.gov.ons.fwmt.resource_service.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.fwmt.resource_service.entity.FieldPeriodEntity;
import uk.gov.ons.fwmt.resource_service.repo.FieldPeriodRepo;
import uk.gov.ons.fwmt.resource_service.service.FieldPeriodService;

import java.util.List;

@Service
public class FieldPeriodServiceImpl implements FieldPeriodService {

    @Autowired
    FieldPeriodRepo fieldPeriodRepo;

    @Override
    public List<FieldPeriodEntity> findFieldPeriods() {
        return fieldPeriodRepo.findAll();
    }

    @Override
    public FieldPeriodEntity createFieldPeriod(FieldPeriodEntity fieldPeriod) {
        return fieldPeriodRepo.save(fieldPeriod);
    }

    @Override
    public FieldPeriodEntity updateFieldPeriod(FieldPeriodEntity fieldPeriod) {
        FieldPeriodEntity fieldPeriodToUpdate = fieldPeriodRepo.findByFieldPeriod(fieldPeriod.getFieldPeriod());
        fieldPeriodToUpdate.setEndDate(fieldPeriod.getEndDate());
        fieldPeriodToUpdate.setStartDate(fieldPeriod.getStartDate());
        return fieldPeriodRepo.save(fieldPeriodToUpdate);
    }

    @Override
    public void deleteFieldPeriod(FieldPeriodEntity fieldPeriod) {
        fieldPeriodRepo.delete(fieldPeriod);
    }

    @Override
    public FieldPeriodEntity findFieldPeriod(String fieldPeriod) {
        return fieldPeriodRepo.findByFieldPeriod(fieldPeriod);
    }
}
