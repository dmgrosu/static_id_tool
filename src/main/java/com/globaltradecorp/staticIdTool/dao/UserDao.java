package com.globaltradecorp.staticIdTool.dao;

import com.globaltradecorp.staticIdTool.controller.RegisterController;
import com.globaltradecorp.staticIdTool.model.AppUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author Dmitri Grosu (dmitri.grosu@codefactorygroup.com), 1/7/21
 */
@Component
public class UserDao {

    private final JdbcTemplate jdbcTemplate;
    private final static Logger logger = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<AppUser> findByUsername(String username) {
        try {
            String sql = "select * from staticid.app_user " +
                    "where username = ? " +
                    "and deleted_at is null";
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new AppUserRowMapper(), username));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    public Optional<AppUser> findByEmail(String email) {
        try {
            String sql = "select * from staticid.app_user where email = ? and deleted_at is null";
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new AppUserRowMapper(), email));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    public int saveUser(AppUser appUser) {
        try {
            if (appUser.isNew()) {
                String sql = "insert into staticid.app_user(username, passwd, first_name, last_name, email)" +
                        "values (?, ?, ?, ?, ?)";
                return jdbcTemplate.update(sql,
                        appUser.getUsername(),
                        appUser.getPasswd(),
                        appUser.getFirstName(),
                        appUser.getLastName(),
                        appUser.getEmail()
                );
            } else {
                String sql = "update staticid.app_user set username=?, passwd=?, first_name=?, last_name=?, email=?, approved_at=? " +
                        "where id=?";
                return jdbcTemplate.update(sql,
                        appUser.getUsername(),
                        appUser.getPasswd(),
                        appUser.getFirstName(),
                        appUser.getLastName(),
                        appUser.getEmail(),
                        appUser.getApprovedAt(),
                        appUser.getId()
                );
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    public boolean usernameExists(String username) {
        try {
            String sql = "select exists(select 1 from staticid.app_user where username=? and deleted_at is null)";
            Boolean queryResult = jdbcTemplate.queryForObject(sql, Boolean.class, username);
            return queryResult != null && queryResult;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    public boolean userEmailExists(String username) {
        try {
            String sql = "select exists(select 1 from staticid.app_user where email=? and deleted_at is null)";
            Boolean queryResult = jdbcTemplate.queryForObject(sql, Boolean.class, username);
            return queryResult != null && queryResult;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        }
    }

}
