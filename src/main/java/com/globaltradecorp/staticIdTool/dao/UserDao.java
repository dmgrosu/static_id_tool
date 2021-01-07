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
            String sql = "select * from staticid.user where username = ?";
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
            String sql = "select * from staticid.user where email = ?";
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
            String sql = "insert into staticid.user(username, passwd, first_name, last_name, email)" +
                    "values (?, ?, ?, ?, ?)";
            return jdbcTemplate.update(sql,
                    appUser.getUsername(),
                    appUser.getPasswd(),
                    appUser.getFirstName(),
                    appUser.getLastName(),
                    appUser.getEmail()
            );
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        }
    }
}
