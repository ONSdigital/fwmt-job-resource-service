package uk.gov.ons.fwmt.resource_service.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.fwmt.resource_service.entity.TMUserEntity;
import uk.gov.ons.fwmt.resource_service.repo.TMUserRepo;
import uk.gov.ons.fwmt.resource_service.service.TMUserService;

import java.util.List;

@Service
public class TMUserServiceImpl implements TMUserService {

    @Autowired
    TMUserRepo tmUserRepo;

    @Override
    public List<TMUserEntity> findUsers() {
        return tmUserRepo.findAll();
    }

    @Override
    public TMUserEntity createUser(TMUserEntity user) {
        return tmUserRepo.save(user);
    }

    @Override
    public TMUserEntity updateUser(TMUserEntity user) {
        TMUserEntity userToUpdate = tmUserRepo.findByAuthNo(user.getAuthNo());
        if(userToUpdate == null) {
            return null;
        }
        userToUpdate.setActive(user.isActive());
        userToUpdate.setAlternateAuthNo(user.getAlternateAuthNo());
        userToUpdate.setAuthNo(user.getAuthNo());
        userToUpdate.setTmUsername(user.getTmUsername());
        return tmUserRepo.save(userToUpdate);
    }

    @Override
    public void deleteUser(TMUserEntity user) {
        tmUserRepo.delete(user);
    }

    @Override
    public TMUserEntity findUserAuthNo(String authNo) {
        return tmUserRepo.findByAuthNo(authNo);
    }

    @Override
    public TMUserEntity findUserAlternateAuthNo(String authNo) {
        return tmUserRepo.findByAlternateAuthNo(authNo);
    }
}
