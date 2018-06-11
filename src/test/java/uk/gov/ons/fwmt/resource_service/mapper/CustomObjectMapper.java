package uk.gov.ons.fwmt.resource_service.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class CustomObjectMapper extends ObjectMapper {

  public CustomObjectMapper() {
    this.registerModule(new JavaTimeModule());
    this.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
  }
}