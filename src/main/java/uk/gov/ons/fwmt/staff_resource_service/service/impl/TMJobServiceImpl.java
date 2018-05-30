package uk.gov.ons.fwmt.staff_resource_service.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.fwmt.staff_resource_service.entity.TMJobEntity;
import uk.gov.ons.fwmt.staff_resource_service.repo.TMJobRepo;
import uk.gov.ons.fwmt.staff_resource_service.service.TMJobService;

import java.util.List;

@Service
public class TMJobServiceImpl implements TMJobService {

    @Autowired
    TMJobRepo tmJobRepo;

    @Override
    public List<TMJobEntity> findJobs() {
        return tmJobRepo.findAll();
    }

    @Override
    public TMJobEntity createJob(TMJobEntity job) {
        return tmJobRepo.save(job);
    }

    @Override
    public TMJobEntity updateJob(TMJobEntity job) {
        return tmJobRepo.save(job);
    }

    @Override
    public void deleteJob(TMJobEntity job) {
        tmJobRepo.delete(job);
    }
}
