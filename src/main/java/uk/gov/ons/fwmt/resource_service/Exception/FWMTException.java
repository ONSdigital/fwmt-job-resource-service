package uk.gov.ons.fwmt.resource_service.Exception;

public class FWMTException extends Exception {

  public enum Error {
    SYSTEM_ERROR,
    RESOURCE_NOT_FOUND,
    ACCESS_DENIED,
    CONFLICT
  }
  private Error error;

  public final Error getError() {
    return error;
  }

  public FWMTException(Error error, String message) {
    super(error.toString() + " " + message);
    this.error = error;
  }

  public FWMTException(Error error, Throwable cause) {
    super(error.toString() + " " + cause);
    this.error = error;
  }
}
