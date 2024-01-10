package com.trytocopyit.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import com.trytocopyit.entity.Acc;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@Transactional
@Repository
public class UserRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public Acc findAccount(String userName) {
        Session session = this.sessionFactory.getCurrentSession();
        return session.find(Acc.class, userName);
    }

    public void save(Acc user) {
        Session session = this.sessionFactory.getCurrentSession();
        // System.out.println("Account= " + user);
        session.persist(user);

    }

}