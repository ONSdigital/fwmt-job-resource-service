package uk.gov.ons.fwmt.resource_service.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class TMUserDTO {
    public String authNo;
    public String tmUsername;
    public boolean active;
    public String alternateAuthNo;
}
