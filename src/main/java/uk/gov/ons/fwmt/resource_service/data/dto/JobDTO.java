package uk.gov.ons.fwmt.resource_service.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobDTO {
  private String tmJobId;
  private String lastAuthNo;
  private LocalDateTime lastUpdated;
}
