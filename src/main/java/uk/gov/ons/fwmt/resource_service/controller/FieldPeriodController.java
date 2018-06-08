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
public class FieldPeriodController {

    @Autowired
    FieldPeriodService fieldPeriodService;

    @Autowired
    MapperFacade mapperfacade;

    @RequestMapping(value = "/fieldperiods", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<FieldPeriodDTO>> getAllFieldPeriods() {
        final List<FieldPeriodEntity> fieldPeriods = fieldPeriodService.findFieldPeriods();
        final List<FieldPeriodDTO> result = mapperfacade.mapAsList(fieldPeriods, FieldPeriodDTO.class);
        return ResponseEntity.ok(result);
    }

    @RequestMapping(value = "/fieldperiods/{fieldPeriod}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<FieldPeriodDTO> getFieldPeriod(@PathVariable("fieldPeriod") String fieldPeriod) {
        final FieldPeriodEntity fieldPeriodEntity = fieldPeriodService.findFieldPeriod(fieldPeriod);
        final FieldPeriodDTO result = mapperfacade.map(fieldPeriodEntity, FieldPeriodDTO.class);
        return ResponseEntity.ok(result);
    }

    @RequestMapping(value = "/fieldperiods", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity createFieldPeriod(@RequestBody FieldPeriodDTO fieldPeriodDTO) {
        if (fieldPeriodService.findFieldPeriod(fieldPeriodDTO.getFieldPeriod()) != null){
            log.info("field period already exists");
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
        FieldPeriodEntity fieldPeriod = fieldPeriodService.createFieldPeriod(mapperfacade.map(fieldPeriodDTO, FieldPeriodEntity.class));
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/fieldperiods", method = RequestMethod.PUT, produces = "application/json", consumes = "application/json")
    public ResponseEntity updateFieldPeriod(@RequestBody FieldPeriodDTO fieldPeriodDTO) {
        FieldPeriodEntity fieldPeriodEntity = fieldPeriodService.updateFieldPeriod(mapperfacade.map(fieldPeriodDTO, FieldPeriodEntity.class));
        if (fieldPeriodEntity == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(fieldPeriodDTO);

    }

    @RequestMapping(value = "/fieldperiods", method = RequestMethod.DELETE, produces = "application/json", consumes = "application/json")
    public ResponseEntity deleteFieldPeriod(@RequestBody FieldPeriodDTO fieldPeriodDTO) {
        final FieldPeriodEntity fieldPeriodToDelete = fieldPeriodService.findFieldPeriod(fieldPeriodDTO.getFieldPeriod());
        if(fieldPeriodToDelete == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        fieldPeriodService.deleteFieldPeriod(fieldPeriodToDelete);
        return ResponseEntity.ok(fieldPeriodDTO);

    }


}
