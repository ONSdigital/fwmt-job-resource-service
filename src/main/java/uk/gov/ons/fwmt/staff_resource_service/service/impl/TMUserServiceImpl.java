package uk.gov.ons.fwmt.staff_resource_service.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.fwmt.staff_resource_service.entity.TMUserEntity;
import uk.gov.ons.fwmt.staff_resource_service.repo.TMUserRepo;
import uk.gov.ons.fwmt.staff_resource_service.service.TMUserService;

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
        return tmUserRepo.save(user);
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
