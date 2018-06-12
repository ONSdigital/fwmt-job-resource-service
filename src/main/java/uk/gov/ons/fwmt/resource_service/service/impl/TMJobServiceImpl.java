package uk.gov.ons.fwmt.resource_service.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.fwmt.resource_service.entity.TMJobEntity;
import uk.gov.ons.fwmt.resource_service.repo.TMJobRepo;
import uk.gov.ons.fwmt.resource_service.service.TMJobService;

import java.util.List;

@Service
public class TMJobServiceImpl implements TMJobService {

  @Autowired
  TMJobRepo tmJobRepo;

  @Override
  public List<TMJobEntity> findJobs() {
    return tmJobRepo.findAll();
  }

  public TMJobEntity findByJobId(String jobId) {
    return tmJobRepo.findByTmJobId(jobId);
  }

  @Override
  public TMJobEntity createJob(TMJobEntity job) {
    return tmJobRepo.save(job);
  }

  @Override
  public TMJobEntity updateJob(TMJobEntity job) {
    TMJobEntity jobToUpdate = tmJobRepo.findByTmJobId(job.getTmJobId());
    if (jobToUpdate == null) {
      return null;
    }
    jobToUpdate.setLastAuthNo(job.getLastAuthNo());
    return tmJobRepo.save(jobToUpdate);
  }

  @Override
  public void deleteJob(TMJobEntity job) {
    tmJobRepo.delete(job);
  }
}
