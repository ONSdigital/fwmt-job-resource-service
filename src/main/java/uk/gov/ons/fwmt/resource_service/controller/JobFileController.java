package uk.gov.ons.fwmt.resource_service.controller;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.ons.fwmt.resource_service.data.dto.JobFileDTO;
import uk.gov.ons.fwmt.resource_service.entity.JobFileEntity;
import uk.gov.ons.fwmt.resource_service.exception.ExceptionCode;
import uk.gov.ons.fwmt.resource_service.exception.FWMTException;
import uk.gov.ons.fwmt.resource_service.service.JobFileService;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/jobFile")
public class JobFileController {

  @Autowired JobFileService jobFileService;

  @Autowired MapperFacade mapperFacade;

  @RequestMapping(value = "/upload", method = RequestMethod.POST, produces = "application/json")
  public ResponseEntity<JobFileDTO> sampleREST(@RequestParam("file") MultipartFile file)
      throws IOException, FWMTException {
    if (jobFileService.getJobFileByName(file.getOriginalFilename()) != null) {
      throw new FWMTException(ExceptionCode.FWMT_RESOURCE_SERVICE_0008, String.format("- Job File %S already exists", file.getOriginalFilename()));
    }
    final JobFileEntity jobFileEntity = jobFileService.storeJobFile(file);
    final JobFileDTO result = mapperFacade.map(jobFileEntity, JobFileDTO.class);
    return ResponseEntity.ok(result);

  }

}
