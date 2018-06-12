package uk.gov.ons.fwmt.resource_service.controller;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.ons.fwmt.resource_service.data.dto.FieldPeriodDTO;
import uk.gov.ons.fwmt.resource_service.entity.FieldPeriodEntity;
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
  public ResponseEntity<FieldPeriodDTO> getFieldPeriod(@PathVariable("fieldPeriod") String fieldPeriod) {
    final FieldPeriodEntity fieldPeriodEntity = fieldPeriodService.findFieldPeriod(fieldPeriod);
    if (fieldPeriodEntity == null) {
      log.warn("field period not found during fetch");
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    final FieldPeriodDTO result = mapperFacade.map(fieldPeriodEntity, FieldPeriodDTO.class);
    return ResponseEntity.ok(result);
  }

  @PostMapping(consumes = "application/json", produces = "application/json")
  public ResponseEntity createFieldPeriod(@RequestBody FieldPeriodDTO fieldPeriodDTO) {
    if (fieldPeriodService.findFieldPeriod(fieldPeriodDTO.getFieldPeriod()) != null) {
      log.warn("field period already exists");
      return new ResponseEntity(HttpStatus.CONFLICT);
    }
    fieldPeriodService.createFieldPeriod(mapperFacade.map(fieldPeriodDTO, FieldPeriodEntity.class));
    return new ResponseEntity(HttpStatus.CREATED);
  }

  @PutMapping(consumes = "application/json", produces = "application/json")
  public ResponseEntity<FieldPeriodDTO> updateFieldPeriod(@RequestBody FieldPeriodDTO fieldPeriodDTO) {
    FieldPeriodEntity fieldPeriodEntity = fieldPeriodService
        .updateFieldPeriod(mapperFacade.map(fieldPeriodDTO, FieldPeriodEntity.class));
    if (fieldPeriodEntity == null) {
      log.warn("field period not found during update");
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return ResponseEntity.ok(fieldPeriodDTO);
  }

  @DeleteMapping(consumes = "application/json", produces = "application/json")
  public ResponseEntity<FieldPeriodDTO> deleteFieldPeriod(@RequestBody FieldPeriodDTO fieldPeriodDTO) {
    final FieldPeriodEntity fieldPeriodToDelete = fieldPeriodService.findFieldPeriod(fieldPeriodDTO.getFieldPeriod());
    if (fieldPeriodToDelete == null) {
      log.warn("field period not found during delete");
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    fieldPeriodService.deleteFieldPeriod(fieldPeriodToDelete);
    return ResponseEntity.ok(fieldPeriodDTO);

  }

}
