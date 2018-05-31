package uk.gov.ons.fwmt.resource_service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.ons.fwmt.resource_service.entity.TMUserEntity;

@Repository
public interface TMUserRepo extends JpaRepository<TMUserEntity, Long> {
    TMUserEntity findByAuthNo(String authNo);
    TMUserEntity findByAlternateAuthNo(String authNo);
}
