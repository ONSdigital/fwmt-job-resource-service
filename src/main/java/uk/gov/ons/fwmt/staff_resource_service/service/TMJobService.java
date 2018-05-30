package uk.gov.ons.fwmt.staff_resource_service.service;

import uk.gov.ons.fwmt.staff_resource_service.entity.TMJobEntity;

import java.util.List;
import java.util.Optional;

public interface TMJobService {
    List<TMJobEntity> findJobs();
    TMJobEntity createJob(TMJobEntity job);
    TMJobEntity updateJob(TMJobEntity job);
    void deleteJob(TMJobEntity job);
}
