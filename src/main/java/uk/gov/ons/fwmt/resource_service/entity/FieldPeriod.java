package uk.gov.ons.fwmt.resource_service.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "field_periods")
public class FieldPeriod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    @Column(nullable = false)
    Date startDate;

    @Column(nullable = false)
    Date endDate;

    @Column(nullable = false)
    String fieldPeriod;
}

