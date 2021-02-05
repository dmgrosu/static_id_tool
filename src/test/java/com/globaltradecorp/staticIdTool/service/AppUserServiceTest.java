package com.globaltradecorp.staticIdTool.service;

import com.globaltradecorp.staticIdTool.dao.UserDao;
import com.globaltradecorp.staticIdTool.model.AppUser;
import com.globaltradecorp.staticIdTool.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class AppUserServiceTest {

    @Mock
    private UserDao userDaoMock;
    @InjectMocks
    private AppUserService appUserService;

    @BeforeEach
    void setUp() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication().getName()).thenReturn("someAuthorizedUsername");
    }

    @Test
    void registerNewUser_daoCalledOnce() {
        // ARRANGE
        when(userDaoMock.rolesExists(any())).thenReturn(true);
        // ACT
        appUserService.registerNewUser(AppUser.builder()
                .firstName("John")
                .lastName("Doe")
                .username("jdoe")
                .roles(Collections.singletonList(Role.USER))
                .build());
        // ASSERT
        verify(userDaoMock, times(1)).saveUser(any(AppUser.class));
        verify(userDaoMock, times(1)).saveRoleForUsername(eq("jdoe"), eq(Role.USER));
    }

    @Test
    void getByUsername_daoCalledOnce() {
        // ACT
        Optional<AppUser> actualUser = appUserService.getByUsername("someUsername");
        // ASSERT
        verify(userDaoMock, times(1)).findByUsername(anyString());
    }

    @Test
    void getByEmail_daoCalledOnce() {
        // ACT
        Optional<AppUser> actualUser = appUserService.getByEmail("someEmail");
        // ASSERT
        verify(userDaoMock, times(1)).findByEmail(anyString());
    }

    @Test
    void getCurrentUser_daoCalledOnce() {
        // ACT
        appUserService.getCurrentUser();
        // ASSERT
        verify(userDaoMock, times(1)).findByUsername(anyString());
    }

    @Test
    void test_usernameExists_daoCalledOnce() {
        // ACT
        appUserService.usernameExists("someUser");
        // ASSERT
        verify(userDaoMock, times(1)).usernameExists(eq("someUser"));
    }

    @Test
    void test_userEmailExists_daoCalledOnce() {
        // ACT
        appUserService.userEmailExists("someEmail");
        // ASSERT
        verify(userDaoMock, times(1)).userEmailExists(eq("someEmail"));
    }
}
