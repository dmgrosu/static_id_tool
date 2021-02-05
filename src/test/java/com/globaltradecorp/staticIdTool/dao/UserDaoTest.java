package com.globaltradecorp.staticIdTool.dao;

import com.globaltradecorp.staticIdTool.model.AppUser;
import com.globaltradecorp.staticIdTool.model.Role;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class UserDaoTest {

    @Mock
    private JdbcTemplate jdbcTemplateMock;
    @InjectMocks
    private UserDao userDao;

    @Test
    void test_usernameExists_checkJdbcParams() {
        // ARRANGE
        String expectedSql = "select exists(select 1 from staticid.app_user where username=? and deleted_at is null)";
        // ACT
        userDao.usernameExists("someUser");
        // ASSERT
        verify(jdbcTemplateMock).queryForObject(eq(expectedSql), eq(Boolean.class), eq("someUser"));
    }

    @Test
    void test_userEmailExists_checkJdbcMethodParams() {
        // ARRANGE
        String expectedSql = "select exists(select 1 from staticid.app_user where email=? and deleted_at is null)";
        // ACT
        userDao.userEmailExists("someEmail");
        // ASSERT
        verify(jdbcTemplateMock).queryForObject(eq(expectedSql), eq(Boolean.class), eq("someEmail"));
    }

    @Test
    void test_saveUser_newUser_checkJdbcMethodParams() {
        // ARRANGE
        String expectedSql = "insert into staticid.app_user(username, passwd, first_name, last_name, email)" +
                "values (?, ?, ?, ?, ?)";
        AppUser givenUser = AppUser.builder()
                .username("username")
                .passwd("passwd")
                .firstName("first")
                .lastName("last")
                .email("email")
                .build();
        // ACT
        int actualResult = userDao.saveUser(givenUser);
        // ASSERT
        verify(jdbcTemplateMock, times(1))
                .update(eq(expectedSql),
                        eq("username"),
                        eq("passwd"),
                        eq("first"),
                        eq("last"),
                        eq("email")
                );
    }

    @Test
    void test_saveUser_existingUser_checkJdbcMethodParams() {
        // ARRANGE
        String expectedSql = "update staticid.app_user set username=?, passwd=?, first_name=?, last_name=?, email=?, approved_at=? " +
                "where id=?";
        OffsetDateTime now = OffsetDateTime.now();
        AppUser givenUser = AppUser.builder()
                .id(1)
                .username("username")
                .passwd("passwd")
                .firstName("first")
                .lastName("last")
                .email("email")
                .approvedAt(now)
                .build();
        // ACT
        int actualResult = userDao.saveUser(givenUser);
        // ASSERT
        verify(jdbcTemplateMock, times(1))
                .update(eq(expectedSql),
                        eq("username"),
                        eq("passwd"),
                        eq("first"),
                        eq("last"),
                        eq("email"),
                        eq(now),
                        eq(1)
                );
    }

    @Test
    void test_findByUsername_checkJdbcMethodParams() {
        // ARRANGE
        String expectedSql = "select u.*, " +
                "(select string_agg(r.name, ',') " +
                "from staticid.app_role r " +
                "join staticid.app_user_role aur on r.id = aur.role_id " +
                "where aur.user_id = u.id and r.deleted_at is null) as roles " +
                "from staticid.app_user as u " +
                "where u.username = ? " +
                "and u.deleted_at is null";
        // ACT
        userDao.findByUsername("someUsername");
        // ASSERT
        verify(jdbcTemplateMock, times(1)).queryForObject(eq(expectedSql), isA(AppUserRowMapper.class), eq("someUsername"));
    }

    @Test
    void test_findByEmail_checkJdbcMethodParams() {
        // ARRANGE
        String expectedSql = "select u.*, " +
                "(select string_agg(r.name, ',') " +
                "from staticid.app_role r " +
                "join staticid.app_user_role aur on r.id = aur.role_id " +
                "where aur.user_id = u.id and r.deleted_at is null) as roles " +
                "from staticid.app_user as u " +
                "where u.email = ? " +
                "and u.deleted_at is null";
        // ACT
        userDao.findByEmail("someEmail");
        // ASSERT
        verify(jdbcTemplateMock, times(1)).queryForObject(eq(expectedSql), isA(AppUserRowMapper.class), eq("someEmail"));
    }

    @Test
    void test_getById_checkJdbcMethodParams() {
        // ARRANGE
        String expectedSql = "select u.*, " +
                "(select string_agg(r.name, ',') " +
                "from staticid.app_role r " +
                "join staticid.app_user_role aur on r.id = aur.role_id " +
                "where aur.user_id = u.id and r.deleted_at is null) as roles " +
                "from staticid.app_user as u " +
                "where u.id = ?";
        // ACT
        AppUser actualUser = userDao.getById(1);
        // ASSERT
        verify(jdbcTemplateMock, times(1)).queryForObject(eq(expectedSql), isA(AppUserRowMapper.class), eq(1));
    }

    @Test
    void test_getAll_checkJdbcMethodParams() {
        // ARRANGE
        String expectedSql = "select u.*, " +
                "(select string_agg(r.name, ',') " +
                "from staticid.app_role r " +
                "join staticid.app_user_role aur on r.id = aur.role_id " +
                "where aur.user_id = u.id and r.deleted_at is null) as roles " +
                "from staticid.app_user as u " +
                "where u.deleted_at is null " +
                "order by u.username";
        // ACT
        userDao.getAll();
        // ASSERT
        verify(jdbcTemplateMock, times(1)).query(eq(expectedSql), isA(AppUserRowMapper.class));
    }

    @Test
    void test_rolesExists_checkJdbcMethodParams() {
        // ARRANGE
        String expectedSql = "select exists(select 1 from staticid.app_role where name in (?) and deleted_at is null)";
        List<Role> someRoles = Arrays.asList(Role.USER, Role.ADMIN);
        // ACT
        userDao.rolesExists(someRoles);
        // ASSERT
        verify(jdbcTemplateMock, times(1)).queryForObject(eq(expectedSql), eq(Boolean.class), eq("USER,ADMIN"));
    }

    @Test
    void test_saveRoleForUsername_daoCalled() {
        // ARRANGE
        String expectedSql = "insert into staticid.app_user_role(user_id, role_id) values (" +
                "(select id from staticid.app_user where username = ?)," +
                "(select id from staticid.app_role where name = ?)" +
                ")";
        // ACT
        userDao.saveRoleForUsername("someUser", Role.USER);
        // ASSERT
        verify(jdbcTemplateMock, times(1)).update(eq(expectedSql), eq("someUser"), eq("USER"));
    }
}
