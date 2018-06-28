package uk.gov.ons.fwmt.resource_service.service;

import org.springframework.web.multipart.MultipartFile;
import uk.gov.ons.fwmt.resource_service.entity.JobFileEntity;

import java.io.IOException;
import java.util.List;

public interface JobFileService {
  List<JobFileEntity> getJobFiles();
  JobFileEntity storeJobFile(MultipartFile file) throws IOException;
}
