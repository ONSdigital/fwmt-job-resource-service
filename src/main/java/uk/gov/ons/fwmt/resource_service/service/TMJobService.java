package uk.gov.ons.fwmt.resource_service.service;

import uk.gov.ons.fwmt.resource_service.entity.TMJobEntity;

import java.util.List;

public interface TMJobService {
    List<TMJobEntity> findJobs();
    TMJobEntity createJob(TMJobEntity job);
    TMJobEntity updateJob(TMJobEntity job);
    void deleteJob(TMJobEntity job);
    TMJobEntity findByJobId(String jobId);
}
