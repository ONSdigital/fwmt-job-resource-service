package uk.gov.ons.fwmt.resource_service.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.ons.fwmt.resource_service.entity.JobFileEntity;
import uk.gov.ons.fwmt.resource_service.repo.JobFileEntityRepo;
import uk.gov.ons.fwmt.resource_service.service.JobFileService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class JobFileServiceImpl implements JobFileService {

  @Autowired private JobFileEntityRepo jobFileEntityRepo;

  @Override public Optional<JobFileEntity> getJobFileByName(String filename) {
    final Optional<JobFileEntity> jobFile = jobFileEntityRepo.findByfilename(filename);
    return jobFile;
  }

  @Override public JobFileEntity storeJobFile(MultipartFile file) throws IOException {
    final String dateRegex = "\\d{4}-\\d{2}-\\d{2}T\\d{2}-\\d{2}-\\d{2}Z";
    final String filename = file.getOriginalFilename();
    final LocalDateTime fileReceivedTime = LocalDateTime.now();
    LocalDateTime fileTime = null;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss'Z'");

    Matcher m = Pattern.compile(dateRegex).matcher(filename);
    if (m.find()) {
      fileTime = LocalDateTime.parse(m.group(), formatter);
    }
    final byte[] fileContents = file.getBytes();

    JobFileEntity jobFileEntity = new JobFileEntity();

    jobFileEntity.setFile(fileContents);
    jobFileEntity.setFilename(filename);
    jobFileEntity.setFileReceivedTime(fileReceivedTime);
    jobFileEntity.setFileTime(fileTime);

    jobFileEntityRepo.save(jobFileEntity);

    return jobFileEntity;
  }
}
