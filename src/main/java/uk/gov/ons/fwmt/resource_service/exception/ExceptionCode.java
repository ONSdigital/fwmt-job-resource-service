package uk.gov.ons.fwmt.resource_service.exception;

public enum ExceptionCode {
  FWMT_RESOURCE_SERVICE_0001("UNKNOWN"),
  FWMT_RESOURCE_SERVICE_0002("UNKNOWN_JOB_ID"),
  FWMT_RESOURCE_SERVICE_0003("UNKNOWN_USER_ID"),
  FWMT_RESOURCE_SERVICE_0004("UNKNOWN_FIELD_PERIOD"),
  FWMT_RESOURCE_SERVICE_0005("USER_ALREADY_EXISTS"),
  FWMT_RESOURCE_SERVICE_0006("JOB_ALREADY_EXISTS"),
  FWMT_RESOURCE_SERVICE_0007("FIELD_PERIOD_ALREADY_EXISTS");

  private final String error;

  ExceptionCode(String error) {
    this.error = error;
  }

  public String getError() {
    return error;
  }
}
