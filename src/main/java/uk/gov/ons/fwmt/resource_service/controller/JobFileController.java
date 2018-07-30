package uk.gov.ons.fwmt.resource_service.controller;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.ons.fwmt.resource_service.data.dto.JobFileDTO;
import uk.gov.ons.fwmt.resource_service.entity.JobFileEntity;
import uk.gov.ons.fwmt.resource_service.service.JobFileService;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/jobFile")
public class JobFileController {

  @Autowired private JobFileService jobFileService;

  @Autowired private MapperFacade mapperFacade;

  @RequestMapping(value = "/upload", method = RequestMethod.POST, produces = "application/json")
  public ResponseEntity<JobFileDTO> storeJobFile(@RequestParam("file") MultipartFile file, boolean validated)
      throws IOException {
    final JobFileEntity jobFileEntity = jobFileService.storeJobFile(file, validated);
    final JobFileDTO result = mapperFacade.map(jobFileEntity, JobFileDTO.class);
    return new ResponseEntity<>(result, HttpStatus.CREATED);

  }

  @GetMapping(value = "/{fileName:.+}")
  public ResponseEntity<Resource> getJobFileByfileName(@PathVariable("fileName") String fileName) {
    final Optional<JobFileEntity> jobFileEntityOptional = jobFileService.getJobFileByName(fileName);
    if(jobFileEntityOptional.isPresent()) {
      final byte[] fileBytes =  jobFileEntityOptional.get().getFile();
      final ByteArrayResource resource = new ByteArrayResource(fileBytes);
      return ResponseEntity.ok().contentLength(fileBytes.length).contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}
