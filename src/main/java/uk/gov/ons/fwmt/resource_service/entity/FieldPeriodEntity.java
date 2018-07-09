package uk.gov.ons.fwmt.resource_service.entity;

import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "field_periods")
public class FieldPeriodEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  @Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
  private LocalDate startDate;

  @Column(nullable = false)
  @Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
  private LocalDate endDate;

  @Column(nullable = false)
  private String fieldPeriod;
}

