package com.trytocopyit.service;

import java.util.HashSet;
import java.util.List;


import com.trytocopyit.entity.Acc;
import com.trytocopyit.repository.UserRepoInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserServiceInterface {

    @Autowired
    private UserRepoInterface repo;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void save(Acc user) {
        user.setEncrytedPassword(bCryptPasswordEncoder.encode(user.getEncrytedPassword()));
        user.setUserRole("ROLE_USER");
        repo.save(user);
    }

    @Override
    public List<Acc> getAllUsers() {
        return repo.findAll();
    }
}