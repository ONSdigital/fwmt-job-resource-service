package uk.gov.ons.fwmt.resource_service.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
  public String authNo;
  public String tmUsername;
  public boolean active;
  public String alternateAuthNo;
}
