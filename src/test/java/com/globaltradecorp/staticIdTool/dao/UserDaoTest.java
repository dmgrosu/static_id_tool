package com.globaltradecorp.staticIdTool.dao;

import com.globaltradecorp.staticIdTool.model.AppUser;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.OffsetDateTime;

import static org.mockito.ArgumentMatchers.eq;
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
}
