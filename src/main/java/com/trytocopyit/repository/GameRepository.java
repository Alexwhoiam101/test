package com.trytocopyit.repository;

import java.io.IOException;
import java.util.Date;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import com.trytocopyit.entity.Game;
import com.trytocopyit.form.GameForm;
import com.trytocopyit.model.GameInfo;
import com.trytocopyit.pagination.PaginationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class GameRepository {

    @Autowired
    private SessionFactory sessionFactory;

    public Game findGame(String code) {
        try {
            String sql = "Select e from " + Game.class.getName() + " e Where e.code =:code ";

            Session session = this.sessionFactory.getCurrentSession();
            Query<Game> query = session.createQuery(sql, Game.class);
            query.setParameter("code", code);
            return (Game) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public GameInfo findGameInfo(String code) {
        Game product = this.findGame(code);
        if (product == null) {
            return null;
        }
        return new GameInfo(product.getCode(), product.getName(), product.getPrice());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void save(GameForm gameForm) {

        Session session = this.sessionFactory.getCurrentSession();
        String code = gameForm.getCode();

        Game game = null;

        boolean isNew = false;
        if (code != null) {
            game = this.findGame(code);
        }
        if (game == null) {
            isNew = true;
            game = new Game();
            game.setCreateDate(new Date());
        }
        game.setCode(code);
        game.setName(gameForm.getName());
        game.setPrice(gameForm.getPrice());

        if (gameForm.getFileData() != null) {
            byte[] image = null;
            try {
                image = gameForm.getFileData().getBytes();
            } catch (IOException e) {
            }
            if (image != null && image.length > 0) {
                game.setImage(image);
            }
        }
        if (isNew) {
            session.persist(game);
        }

        session.flush();
    }

    public PaginationResult<GameInfo> queryGame(int page, int maxResult, int maxNavigationPage, String likeName) {
        String sql = "Select new " + GameInfo.class.getName() //
                + "(p.code, p.name, p.price) " + " from "//
                + Game.class.getName() + " p ";
        if (likeName != null && likeName.length() > 0) {
            sql += " Where lower(p.name) like :likeName ";
        }
        sql += " order by p.createDate desc ";
        //
        Session session = this.sessionFactory.getCurrentSession();
        Query<GameInfo> query = session.createQuery(sql, GameInfo.class);

        if (likeName != null && likeName.length() > 0) {
            query.setParameter("likeName", "%" + likeName.toLowerCase() + "%");
        }
        return new PaginationResult<GameInfo>(query, page, maxResult, maxNavigationPage);
    }

    public PaginationResult<GameInfo> queryGame(int page, int maxResult, int maxNavigationPage) {
        return queryGame(page, maxResult, maxNavigationPage, null);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void delete(GameForm gameForm) {
        Session session = this.sessionFactory.getCurrentSession();
        String code = gameForm.getCode();

        Game game = null;

        boolean isNew = false;
        if (code != null) {
            game = this.findGame(code);
            session.delete(game);
        }

        session.flush();
    }
}
