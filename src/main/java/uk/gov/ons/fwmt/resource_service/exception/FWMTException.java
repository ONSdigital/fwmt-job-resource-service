package uk.gov.ons.fwmt.resource_service.exception;

public class FWMTException extends Exception {

  private ExceptionCode error;

  public FWMTException(ExceptionCode error, String message) {
    super(error.toString() + " " + message);
    this.error = error;
  }

  public FWMTException(ExceptionCode error, Throwable cause) {
    super(error.toString() + " " + cause);
    this.error = error;
  }

  public final ExceptionCode getError() {
    return error;
  }
}
