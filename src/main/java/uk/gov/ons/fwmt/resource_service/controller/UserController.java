package uk.gov.ons.fwmt.resource_service.controller;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.ons.fwmt.resource_service.data.dto.UserDTO;
import uk.gov.ons.fwmt.resource_service.entity.TMUserEntity;
import uk.gov.ons.fwmt.resource_service.service.TMUserService;

import java.util.List;

@Slf4j
@RestController
public class UserController {
    @Autowired
    TMUserService userService;

    @Autowired
    MapperFacade mapperfacade;

    @RequestMapping(value = "/users", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<UserDTO>> getUsers() {
        List<TMUserEntity> users = userService.findUsers();
        List<UserDTO> result = mapperfacade.mapAsList(users, UserDTO.class);
        return ResponseEntity.ok(result);
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity createUser(@RequestBody UserDTO userDTO) {
        if (userService.findUserAuthNo(userDTO.getAuthNo()) != null){
            log.info("user already exists");
            return new ResponseEntity(HttpStatus.CONFLICT);
        }

        TMUserEntity user = userService.createUser(mapperfacade.map(userDTO, TMUserEntity.class));
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/users", method = RequestMethod.PUT, produces = "application/json", consumes = "application/json")
    public ResponseEntity updateUser(@RequestBody UserDTO userDTO) {
        TMUserEntity user = userService.updateUser(mapperfacade.map(userDTO, TMUserEntity.class));
        return ResponseEntity.ok(userDTO);
    }

    @RequestMapping(value = "/users", method = RequestMethod.DELETE, produces = "application/json", consumes = "application/json")
    public ResponseEntity<UserDTO> deleteUser(@RequestBody UserDTO userDTO) {
        TMUserEntity userToDelete = userService.findUserAuthNo(userDTO.getAuthNo());
        if(userToDelete == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        userService.deleteUser(userToDelete);
        return ResponseEntity.ok(userDTO);
    }

    @RequestMapping(value = "/users/auth/{authNo}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<UserDTO> getUserByAuthNo(@PathVariable("authNo") String authNo) {
        TMUserEntity user = userService.findUserAuthNo(authNo);
        if (user == null) {
            log.info("user not found");
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        UserDTO result = mapperfacade.map(user, UserDTO.class);
        return ResponseEntity.ok(result);
    }

    @RequestMapping(value = "/users/alternative/{altAuthNo}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<UserDTO> getUserByAltAuthNo(@PathVariable("altAuthNo") String altAuthNo) {
        TMUserEntity user = userService.findUserAlternateAuthNo(altAuthNo);
        if (user == null) {
            log.info("user not found");
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        UserDTO result = mapperfacade.map(user, UserDTO.class);
        return ResponseEntity.ok(result);
    }

}
