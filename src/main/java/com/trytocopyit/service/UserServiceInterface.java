package com.trytocopyit.service;

import com.trytocopyit.entity.Acc;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserServiceInterface {
    void save(Acc user);
    List<Acc> getAllUsers();
}
