package uk.gov.ons.fwmt.resource_service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.ons.fwmt.resource_service.entity.JobFileEntity;

import java.util.Optional;

public interface JobFileEntityRepo extends JpaRepository<JobFileEntity, Long> {
  Optional<JobFileEntity> findByfilename(String filename);
}
