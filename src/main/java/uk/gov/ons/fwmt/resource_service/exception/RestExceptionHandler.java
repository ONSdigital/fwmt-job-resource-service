package uk.gov.ons.fwmt.resource_service.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler {
  public ResponseEntity<GatewayCommonErrorDTO> makeCommonError(HttpServletRequest request, Throwable exception,
      HttpStatus status, String error, String message) {
    GatewayCommonErrorDTO errorDTO = new GatewayCommonErrorDTO();
    errorDTO.setError(error);
    errorDTO.setException(exception.getClass().getName());
    errorDTO.setMessage(message);
    errorDTO.setPath(request.getRequestURI());
    errorDTO.setStatus(status.value());
    errorDTO.setTimestamp(LocalDateTime.now().toString());
    return new ResponseEntity<>(errorDTO, status);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<GatewayCommonErrorDTO> handleAnyException(HttpServletRequest request, Exception exception) {
    log.error(ExceptionCode.FWMT_JOB_SERVICE_0001.toString()+" "+ExceptionCode.FWMT_JOB_SERVICE_0001.getError(), exception);
    return makeCommonError(request, exception, HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error", "Unknown error");
  }

  @ExceptionHandler(FWMTException.class)
  public ResponseEntity<GatewayCommonErrorDTO> handleFWMTException(HttpServletRequest request, FWMTException exception) {
    HttpStatus status;
    switch (exception.getError()) {
      case RESOURCE_NOT_FOUND:
        status = HttpStatus.NOT_FOUND;
        break;
      case CONFLICT:
        status = HttpStatus.CONFLICT;
        break;
      case ACCESS_DENIED:
        status = HttpStatus.UNAUTHORIZED;
        break;
      case SYSTEM_ERROR:
        status = HttpStatus.INTERNAL_SERVER_ERROR;
        break;
      default:
        status = HttpStatus.NO_CONTENT;
        break;
    }
    return makeCommonError(request, exception, status, exception.getError().toString(), exception.getMessage());

  }

}

