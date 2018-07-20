package uk.gov.ons.fwmt.resource_service.service;

import org.springframework.web.multipart.MultipartFile;
import uk.gov.ons.fwmt.resource_service.entity.JobFileEntity;

import java.io.IOException;
import java.util.Optional;

public interface JobFileService {
  Optional<JobFileEntity> getJobFileByName(String filename);

  JobFileEntity storeJobFile(MultipartFile file) throws IOException;
}
