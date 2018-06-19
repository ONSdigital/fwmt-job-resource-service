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
import uk.gov.ons.fwmt.resource_service.data.dto.UserDTO;
import uk.gov.ons.fwmt.resource_service.entity.TMUserEntity;
import uk.gov.ons.fwmt.resource_service.exception.ExceptionCode;
import uk.gov.ons.fwmt.resource_service.exception.FWMTException;
import uk.gov.ons.fwmt.resource_service.service.TMUserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
  @Autowired
  TMUserService userService;

  @Autowired
  MapperFacade mapperfacade;

  @GetMapping(produces = "application/json")
  public ResponseEntity<List<UserDTO>> getUsers() {
    List<TMUserEntity> users = userService.findUsers();
    List<UserDTO> result = mapperfacade.mapAsList(users, UserDTO.class);
    return ResponseEntity.ok(result);
  }

  @PostMapping(consumes = "application/json", produces = "application/json")
  public ResponseEntity createUser(@RequestBody UserDTO userDTO) throws FWMTException {
    if (userService.findUserAuthNo(userDTO.getAuthNo()) != null) {
      log.warn(ExceptionCode.FWMT_JOB_SERVICE_0012 + String.format(" - User %S already exists", userDTO.getAuthNo()));
      throw new FWMTException(FWMTException.Error.CONFLICT, "User already exists");
    }

    userService.createUser(mapperfacade.map(userDTO, TMUserEntity.class));
    return new ResponseEntity(HttpStatus.CREATED);
  }

  @PutMapping(consumes = "application/json", produces = "application/json")
  public ResponseEntity updateUser(@RequestBody UserDTO userDTO) throws FWMTException {
    TMUserEntity user = userService.updateUser(mapperfacade.map(userDTO, TMUserEntity.class));
    if (user == null) {
      log.warn(ExceptionCode.FWMT_JOB_SERVICE_0005 + String.format(" - User %S not found", userDTO.getAuthNo()));
      throw new FWMTException(FWMTException.Error.RESOURCE_NOT_FOUND, "User not found");
    }
    return ResponseEntity.ok(userDTO);
  }

  @DeleteMapping(consumes = "application/json", produces = "application/json")
  public ResponseEntity<UserDTO> deleteUser(@RequestBody UserDTO userDTO) throws FWMTException {
    TMUserEntity userToDelete = userService.findUserAuthNo(userDTO.getAuthNo());
    if (userToDelete == null) {
      log.warn(ExceptionCode.FWMT_JOB_SERVICE_0005 + String.format(" - User %S not found", userDTO.getAuthNo()));
      throw new FWMTException(FWMTException.Error.RESOURCE_NOT_FOUND, "User not found");
    }
    userService.deleteUser(userToDelete);
    return ResponseEntity.ok(userDTO);
  }

  @GetMapping(value = "/auth/{authNo}", produces = "application/json")
  public ResponseEntity<UserDTO> getUserByAuthNo(@PathVariable("authNo") String authNo) throws FWMTException {
    TMUserEntity user = userService.findUserAuthNo(authNo);
    if (user == null) {
      log.warn(ExceptionCode.FWMT_JOB_SERVICE_0005 + String.format(" - User %S not found by authNo", authNo));
      throw new FWMTException(FWMTException.Error.RESOURCE_NOT_FOUND, "User not found");
    }
    UserDTO result = mapperfacade.map(user, UserDTO.class);
    return ResponseEntity.ok(result);
  }

  @GetMapping(value = "/alternative/{altAuthNo}", produces = "application/json")
  public ResponseEntity<UserDTO> getUserByAltAuthNo(@PathVariable("altAuthNo") String altAuthNo) throws FWMTException {
    TMUserEntity user = userService.findUserAlternateAuthNo(altAuthNo);
    if (user == null) {
      log.warn(ExceptionCode.FWMT_JOB_SERVICE_0005 + String.format(" - User %S not found by alternate authNo", altAuthNo));
      throw new FWMTException(FWMTException.Error.RESOURCE_NOT_FOUND, "User not found");
    }
    UserDTO result = mapperfacade.map(user, UserDTO.class);
    return ResponseEntity.ok(result);
  }

}
