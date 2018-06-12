package uk.gov.ons.fwmt.resource_service.service;

import uk.gov.ons.fwmt.resource_service.entity.TMUserEntity;

import java.util.List;

public interface TMUserService {
  List<TMUserEntity> findUsers();

  TMUserEntity createUser(TMUserEntity user);

  TMUserEntity updateUser(TMUserEntity user);

  void deleteUser(TMUserEntity user);

  TMUserEntity findUserAuthNo(String authNo);

  TMUserEntity findUserAlternateAuthNo(String authNo);

}
