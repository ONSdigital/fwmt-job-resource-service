package uk.gov.ons.fwmt.resource_service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.ons.fwmt.resource_service.entity.TMJobEntity;

@Repository
public interface TMJobRepo extends JpaRepository<TMJobEntity, Long> {
    TMJobEntity findByTmJobId(String tmJobId);
}
