package uk.gov.ons.fwmt.resource_service.controller;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.ons.fwmt.resource_service.data.dto.FieldPeriodDTO;
import uk.gov.ons.fwmt.resource_service.entity.FieldPeriodEntity;
import uk.gov.ons.fwmt.resource_service.exception.ExceptionCode;
import uk.gov.ons.fwmt.resource_service.exception.FWMTException;
import uk.gov.ons.fwmt.resource_service.service.FieldPeriodService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/fieldPeriods")
public class FieldPeriodController {

  @Autowired
  FieldPeriodService fieldPeriodService;

  @Autowired
  MapperFacade mapperFacade;

  @GetMapping(produces = "application/json")
  public ResponseEntity<List<FieldPeriodDTO>> getAllFieldPeriods() {
    final List<FieldPeriodEntity> fieldPeriods = fieldPeriodService.findFieldPeriods();
    final List<FieldPeriodDTO> result = mapperFacade.mapAsList(fieldPeriods, FieldPeriodDTO.class);
    return ResponseEntity.ok(result);
  }

  @GetMapping(value = "/{fieldPeriod}", produces = "application/json")
  public ResponseEntity<FieldPeriodDTO> getFieldPeriod(@PathVariable("fieldPeriod") String fieldPeriod)
      throws FWMTException {
    final FieldPeriodEntity fieldPeriodEntity = fieldPeriodService.findFieldPeriod(fieldPeriod);
    if (fieldPeriodEntity == null) {
      log.warn(ExceptionCode.FWMT_JOB_SERVICE_0011 + String.format(" - Field Period %S not found", fieldPeriod));
      throw new FWMTException(FWMTException.Error.RESOURCE_NOT_FOUND, String.format("%S not found", fieldPeriod));
    }
    final FieldPeriodDTO result = mapperFacade.map(fieldPeriodEntity, FieldPeriodDTO.class);
    return ResponseEntity.ok(result);
  }

  @PostMapping(consumes = "application/json", produces = "application/json")
  public ResponseEntity createFieldPeriod(@RequestBody FieldPeriodDTO fieldPeriodDTO) throws FWMTException {
    if (fieldPeriodService.findFieldPeriod(fieldPeriodDTO.getFieldPeriod()) != null) {
      log.warn(ExceptionCode.FWMT_JOB_SERVICE_0014 + String.format(" - Field Period %S already exists", fieldPeriodDTO.getFieldPeriod()));
      throw new FWMTException(FWMTException.Error.CONFLICT, "Field Period already exists");
    }
    fieldPeriodService.createFieldPeriod(mapperFacade.map(fieldPeriodDTO, FieldPeriodEntity.class));
    return new ResponseEntity(HttpStatus.CREATED);
  }

  @PutMapping(consumes = "application/json", produces = "application/json")
  public ResponseEntity<FieldPeriodDTO> updateFieldPeriod(@RequestBody FieldPeriodDTO fieldPeriodDTO)
      throws FWMTException {
    FieldPeriodEntity fieldPeriodEntity = fieldPeriodService
        .updateFieldPeriod(mapperFacade.map(fieldPeriodDTO, FieldPeriodEntity.class));
    if (fieldPeriodEntity == null) {
      log.warn(ExceptionCode.FWMT_JOB_SERVICE_0011 + String.format(" - Field Period %S not found", fieldPeriodDTO.getFieldPeriod()));
      throw new FWMTException(FWMTException.Error.RESOURCE_NOT_FOUND, "Field Period not found");
    }
    return ResponseEntity.ok(fieldPeriodDTO);
  }

  @DeleteMapping(consumes = "application/json", produces = "application/json")
  public ResponseEntity<FieldPeriodDTO> deleteFieldPeriod(@RequestBody FieldPeriodDTO fieldPeriodDTO)
      throws FWMTException {
    final FieldPeriodEntity fieldPeriodToDelete = fieldPeriodService.findFieldPeriod(fieldPeriodDTO.getFieldPeriod());
    if (fieldPeriodToDelete == null) {
      log.warn(ExceptionCode.FWMT_JOB_SERVICE_0011 + String.format(" - Field Period %S not found", fieldPeriodDTO.getFieldPeriod()));
      throw new FWMTException(FWMTException.Error.RESOURCE_NOT_FOUND, "Field Period not found");
    }
    fieldPeriodService.deleteFieldPeriod(fieldPeriodToDelete);
    return ResponseEntity.ok(fieldPeriodDTO);

  }

}
