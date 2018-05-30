package uk.gov.ons.fwmt.resource_service.data.dto;

import lombok.Data;

@Data
public class UserDTO {
    public String authNo;
    public String tmUsername;
    public boolean active;
    public String alternateAuthNo;
}
