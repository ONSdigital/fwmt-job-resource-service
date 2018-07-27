package uk.gov.ons.fwmt.resource_service.entity;

import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tm_jobs")
public class TMJobEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String tmJobId;

  @Column(nullable = false)
  private String lastAuthNo;

  @Column(nullable = false)
  @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
  private LocalDateTime lastUpdated;
}
