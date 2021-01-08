package com.globaltradecorp.staticIdTool.service;

import com.globaltradecorp.staticIdTool.dao.UserDao;
import com.globaltradecorp.staticIdTool.model.AppUser;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class AppUserServiceTest {

    @Mock
    private UserDao userDaoMock;
    @InjectMocks
    private AppUserService appUserService;

    @Test
    void registerNewUser_daoCalledOnce() {
        // ACT
        appUserService.registerNewUser(AppUser.builder()
                .firstName("John")
                .lastName("Doe")
                .build());
        // ASSERT
        verify(userDaoMock, times(1)).saveUser(any(AppUser.class));
    }

    @Test
    void getByUsername_daoCalledOnce() {
        // ACT
        appUserService.getByUsername("someUsername");
        // ASSERT
        verify(userDaoMock, times(1)).findByUsername(anyString());
    }

    @Test
    void getByEmail_daoCalledOnce() {
        // ACT
        appUserService.getByEmail("someEmail");
        // ASSERT
        verify(userDaoMock, times(1)).findByEmail(anyString());
    }
}
