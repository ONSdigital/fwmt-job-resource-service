package uk.gov.ons.fwmt.resource_service.controller;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.ons.fwmt.resource_service.data.dto.JobDTO;
import uk.gov.ons.fwmt.resource_service.entity.TMJobEntity;
import uk.gov.ons.fwmt.resource_service.service.TMJobService;

import java.util.List;

@Slf4j
@RestController
public class JobController {

    @Autowired
    TMJobService jobService;

    @Autowired
    MapperFacade mapperfacade;

    @RequestMapping(value = "/jobs", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<JobDTO>> getJobs() {
        final List<TMJobEntity> jobs = jobService.findJobs();
        final List<JobDTO> result = mapperfacade.mapAsList(jobs, JobDTO.class);
        return ResponseEntity.ok(result);
    }

    @RequestMapping(value = "/jobs", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity createJob(@RequestBody JobDTO jobDTO) {
        if (jobService.findByJobId(jobDTO.getTmJobId()) != null){
            log.info("job already exists");
            return new ResponseEntity(HttpStatus.CONFLICT);
        }

        final TMJobEntity job = jobService.createJob(mapperfacade.map(jobDTO, TMJobEntity.class));
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/jobs", method = RequestMethod.PUT, produces = "application/json", consumes = "application/json")
    public ResponseEntity updateJob(@RequestBody JobDTO jobDTO) {
        final TMJobEntity job = jobService.updateJob(mapperfacade.map(jobDTO, TMJobEntity.class));
        if (job == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(jobDTO);
    }

    @RequestMapping(value = "/jobs", method = RequestMethod.DELETE, produces = "application/json", consumes = "application/json")
    public ResponseEntity<JobDTO> deleteJob(@RequestBody JobDTO jobDTO) {
        final TMJobEntity jobToDelete = jobService.findByJobId(jobDTO.getTmJobId());
        if(jobToDelete == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        jobService.deleteJob(jobToDelete);
        return ResponseEntity.ok(jobDTO);
    }

    @RequestMapping(value = "/jobs/{jobId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<JobDTO> getJobByJobId(@PathVariable("jobId") String jobId) {
        final TMJobEntity job = jobService.findByJobId(jobId);
        if (job == null) return new ResponseEntity(HttpStatus.NOT_FOUND);
        final JobDTO result = mapperfacade.map(job, JobDTO.class);
        return ResponseEntity.ok(result);
    }
}
