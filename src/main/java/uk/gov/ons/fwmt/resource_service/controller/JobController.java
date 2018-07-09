package uk.gov.ons.fwmt.resource_service.controller;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.ons.fwmt.resource_service.data.dto.JobDTO;
import uk.gov.ons.fwmt.resource_service.entity.TMJobEntity;
import uk.gov.ons.fwmt.resource_service.exception.ExceptionCode;
import uk.gov.ons.fwmt.resource_service.exception.FWMTException;
import uk.gov.ons.fwmt.resource_service.service.TMJobService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/jobs")
public class JobController {

  @Autowired
  TMJobService jobService;

  @Autowired
  MapperFacade mapperfacade;

  @GetMapping(produces = "application/json")
  public ResponseEntity<List<JobDTO>> getJobs() {
    final List<TMJobEntity> jobs = jobService.findJobs();
    final List<JobDTO> result = mapperfacade.mapAsList(jobs, JobDTO.class);
    return ResponseEntity.ok(result);
  }

  @PostMapping(produces = "application/json", consumes = "application/json")
  public ResponseEntity createJob(@RequestBody JobDTO jobDTO) throws FWMTException {
    if (jobService.findByJobId(jobDTO.getTmJobId()) != null) {
      log.warn(
          ExceptionCode.FWMT_RESOURCE_SERVICE_0006 + String.format(" - Job %S already exists", jobDTO.getTmJobId()));
      throw new FWMTException(ExceptionCode.FWMT_RESOURCE_SERVICE_0006,
          String.format("- Job %S already exists", jobDTO.getTmJobId()));
    }
    jobService.createJob(mapperfacade.map(jobDTO, TMJobEntity.class));
    return new ResponseEntity(HttpStatus.CREATED);
  }

  @PutMapping(produces = "application/json", consumes = "application/json")
  public ResponseEntity updateJob(@RequestBody JobDTO jobDTO) throws FWMTException {
    final TMJobEntity job = jobService.updateJob(mapperfacade.map(jobDTO, TMJobEntity.class));
    if (job == null) {
      log.warn(ExceptionCode.FWMT_RESOURCE_SERVICE_0002 + String.format(" - Job %S not found", jobDTO.getTmJobId()));
      throw new FWMTException(ExceptionCode.FWMT_RESOURCE_SERVICE_0002,
          String.format("- Job %S not found", jobDTO.getTmJobId()));
    }
    return ResponseEntity.ok(jobDTO);
  }

  @DeleteMapping(produces = "application/json", consumes = "application/json")
  public ResponseEntity<JobDTO> deleteJob(@RequestBody JobDTO jobDTO) throws FWMTException {
    final TMJobEntity jobToDelete = jobService.findByJobId(jobDTO.getTmJobId());
    if (jobToDelete == null) {
      log.warn(ExceptionCode.FWMT_RESOURCE_SERVICE_0002 + String.format(" - Job %S not found", jobDTO.getTmJobId()));
      throw new FWMTException(ExceptionCode.FWMT_RESOURCE_SERVICE_0002,
          String.format("- Job %S not found", jobDTO.getTmJobId()));
    }
    jobService.deleteJob(jobToDelete);
    return ResponseEntity.ok(jobDTO);
  }

  @GetMapping(value = "/{jobId}", produces = "application/json")
  public ResponseEntity<JobDTO> getJobByJobId(@PathVariable("jobId") String jobId) throws FWMTException {
    final TMJobEntity job = jobService.findByJobId(jobId);
    if (job == null) {
      log.warn(ExceptionCode.FWMT_RESOURCE_SERVICE_0002 + String.format(" - Job %S not found", jobId));
      throw new FWMTException(ExceptionCode.FWMT_RESOURCE_SERVICE_0002, String.format("- Job %S not found", jobId));
    }
    final JobDTO result = mapperfacade.map(job, JobDTO.class);
    return ResponseEntity.ok(result);
  }
}
