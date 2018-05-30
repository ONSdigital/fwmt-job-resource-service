package uk.gov.ons.fwmt.resource_service.entity;

import lombok.Data;

import javax.persistence.*;

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
}
