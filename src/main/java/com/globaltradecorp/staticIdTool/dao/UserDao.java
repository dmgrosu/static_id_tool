package com.globaltradecorp.staticIdTool.dao;

import com.globaltradecorp.staticIdTool.model.AppUser;
import com.globaltradecorp.staticIdTool.model.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Dmitri Grosu (dmitri.grosu@codefactorygroup.com), 1/7/21
 */
@Component
@Slf4j
public class UserDao {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Sub-query, returning user roles separated by comma, ex. 'USER,ADMIN'
     */
    private final String USER_WITH_ROLES_QUERY = "select u.*, " +
            "(select string_agg(r.name, ',') " +
            "from staticid.app_role r " +
            "join staticid.app_user_role aur on r.id = aur.role_id " +
            "where aur.user_id = u.id and r.deleted_at is null) as roles " +
            "from staticid.app_user as u ";

    @Autowired
    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<AppUser> findByUsername(String username) {
        try {
            String sql = USER_WITH_ROLES_QUERY +
                    "where u.username = ? " +
                    "and u.deleted_at is null";
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new AppUserRowMapper(), username));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    public Optional<AppUser> findByEmail(String email) {
        try {
            String sql = USER_WITH_ROLES_QUERY +
                    "where u.email = ? and u.deleted_at is null";
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new AppUserRowMapper(), email));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    /**
     * Saves provided user (new or existing) in database
     *
     * @param appUser - user object
     * @return number of rows affected in database
     */
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
            log.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    public boolean rolesExists(List<Role> roles) {
        try {
            String rolesJoined = roles.stream()
                    .map(Role::getName)
                    .collect(Collectors.joining(","));
            String sql = "select exists(select 1 from staticid.app_role where name in (?) and deleted_at is null)";
            Boolean queryResult = jdbcTemplate.queryForObject(sql, Boolean.class, rolesJoined);
            return queryResult != null && queryResult;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    public boolean usernameExists(String username) {
        try {
            String sql = "select exists(select 1 from staticid.app_user where username=? and deleted_at is null)";
            Boolean queryResult = jdbcTemplate.queryForObject(sql, Boolean.class, username);
            return queryResult != null && queryResult;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    public boolean userEmailExists(String username) {
        try {
            String sql = "select exists(select 1 from staticid.app_user where email=? and deleted_at is null)";
            Boolean queryResult = jdbcTemplate.queryForObject(sql, Boolean.class, username);
            return queryResult != null && queryResult;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    public AppUser getById(int userId) {
        try {
            String sql = USER_WITH_ROLES_QUERY +
                    "where u.id = ?";
            return jdbcTemplate.queryForObject(sql, new AppUserRowMapper(), userId);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    public List<AppUser> getAll() {
        try {
            String sql = USER_WITH_ROLES_QUERY +
                    "where u.deleted_at is null " +
                    "order by u.username";
            return jdbcTemplate.query(sql, new AppUserRowMapper());
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    public void approveUser(Integer userId, OffsetDateTime userTime) {
        try {
            String sql = "update staticid.app_user set approved_at=? where id=?";
            jdbcTemplate.update(sql, userTime, userId);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw ex;
        }
    }

    public void deleteUser(Integer userId, OffsetDateTime userTime) {
        try {
            String sql = "update staticid.app_user set deleted_at=? where id=?";
            jdbcTemplate.update(sql, userTime, userId);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw ex;
        }
    }

    public void saveRoleForUsername(String username, Role role) {
        try {
            String sql = "insert into staticid.app_user_role(user_id, role_id) values (" +
                    "(select id from staticid.app_user where username = ?)," +
                    "(select id from staticid.app_role where name = ?)" +
                    ")";
            jdbcTemplate.update(sql, username, role.getName());
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw ex;
        }
    }
}
