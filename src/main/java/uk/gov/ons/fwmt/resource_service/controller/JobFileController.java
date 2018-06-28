package uk.gov.ons.fwmt.resource_service.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.gov.ons.fwmt.resource_service.data.dto.JobFileDTO;
import uk.gov.ons.fwmt.resource_service.service.JobFileService;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/jobFile")
public class JobFileController {

  @Autowired JobFileService jobFileService;

  @RequestMapping(value = "/upload", method = RequestMethod.POST, produces = "application/json")
  public ResponseEntity<JobFileDTO> sampleREST(@RequestParam("file") MultipartFile file) throws IOException {
    jobFileService.storeJobFile(file);
    return ResponseEntity.ok(new JobFileDTO());

  }

}
