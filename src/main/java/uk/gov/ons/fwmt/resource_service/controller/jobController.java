package uk.gov.ons.fwmt.resource_service.controller;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.ons.fwmt.resource_service.data.dto.TMJobDTO;
import uk.gov.ons.fwmt.resource_service.entity.TMJobEntity;
import uk.gov.ons.fwmt.resource_service.service.TMJobService;

import java.util.List;

@Slf4j
@RestController
public class jobController {

    @Autowired
    TMJobService jobService;

    @Autowired
    MapperFacade mapperfacade;

    @RequestMapping(value = "/jobs", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<TMJobDTO>> getJobs() {
        List<TMJobEntity> jobs = jobService.findJobs();
        List<TMJobDTO> result = mapperfacade.mapAsList(jobs, TMJobDTO.class);
        return ResponseEntity.ok(result);
    }

    @RequestMapping(value = "/jobs", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity createJob(@RequestBody TMJobDTO jobDTO) {
        if (jobService.findByJobId(jobDTO.getTmJobId()) != null){
            log.info("job already exists");
            return new ResponseEntity(HttpStatus.CONFLICT);
        }

        TMJobEntity job = jobService.createJob(mapperfacade.map(jobDTO, TMJobEntity.class));
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/jobs", method = RequestMethod.PUT, produces = "application/json", consumes = "application/json")
    public ResponseEntity updateJob(@RequestBody TMJobDTO jobDTO) {
        TMJobEntity job = jobService.updateJob(mapperfacade.map(jobDTO, TMJobEntity.class));
        return ResponseEntity.ok(jobDTO);
    }

    @RequestMapping(value = "/jobs", method = RequestMethod.DELETE, produces = "application/json", consumes = "application/json")
    public ResponseEntity<TMJobDTO> deleteJob(@RequestBody TMJobDTO jobDTO) {
        TMJobEntity jobToDelete = jobService.findByJobId(jobDTO.getTmJobId());
        if(jobToDelete == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        jobService.deleteJob(jobToDelete);
        return ResponseEntity.ok(jobDTO);
    }

    @RequestMapping(value = "/jobs/{jobId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<TMJobDTO> getJobByJobId(@PathVariable("jobId") String jobId) {
        TMJobEntity job = jobService.findByJobId(jobId);
        if (job == null) return new ResponseEntity(HttpStatus.NOT_FOUND);
        TMJobDTO result = mapperfacade.map(job, TMJobDTO.class);
        return ResponseEntity.ok(result);
    }
}
