package com.trytocopyit.repository;

import com.trytocopyit.entity.Acc;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Repository
public interface UserRepoInterface extends JpaRepository<Acc, Long>{
}