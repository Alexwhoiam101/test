package com.trytocopyit.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import com.trytocopyit.entity.Acc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class UserRepository {

    @Autowired
    private SessionFactory sessionFactory;

    public Acc findAccount(String userName) {
        Session session = this.sessionFactory.getCurrentSession();
        return session.find(Acc.class, userName);
    }

}