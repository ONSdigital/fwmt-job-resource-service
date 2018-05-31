package uk.gov.ons.fwmt.resource_service.controller;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
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
    public ResponseEntity<List<FieldPeriodDTO>> getJobs() {
        final List<FieldPeriodEntity> fieldPeriods = fieldPeriodService.findFieldPeriods();
        final List<FieldPeriodDTO> result = mapperfacade.mapAsList(fieldPeriods, FieldPeriodDTO.class);
        return ResponseEntity.ok(result);
    }


}
