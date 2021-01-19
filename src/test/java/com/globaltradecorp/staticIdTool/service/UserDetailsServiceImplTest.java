package com.globaltradecorp.staticIdTool.service;

import com.globaltradecorp.staticIdTool.dao.UserDao;
import com.globaltradecorp.staticIdTool.model.AppUser;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserDetailsServiceImplTest {

    @Mock
    private UserDao userDaoMock;
    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void test_loadUserByUsername_found_userReturned() {
        // ARRANGE
        String username = "jdoe";
        when(userDaoMock.findByUsername(eq(username)))
                .thenReturn(Optional.of(AppUser.builder()
                        .username("jdoe")
                        .passwd("passwd")
                        .firstName("John")
                        .lastName("Doe")
                        .approvedAt(OffsetDateTime.now())
                        .roles(Collections.singletonList("USER"))
                        .build())
                );

        // ACT
        UserDetails actualDetails = userDetailsService.loadUserByUsername("jdoe");

        // ASSERT
        assertEquals("jdoe", actualDetails.getUsername());
        assertEquals("passwd", actualDetails.getPassword());
        assertIterableEquals(Collections.singletonList(new SimpleGrantedAuthority("USER")), actualDetails.getAuthorities());
    }

    @Test
    void test_loadUserByUsername_foundNotApproved_exceptionThrown() {
        // ARRANGE
        String username = "jdoe";
        when(userDaoMock.findByUsername(eq(username)))
                .thenReturn(Optional.of(AppUser.builder()
                        .username("jdoe")
                        .passwd("passwd")
                        .firstName("John")
                        .lastName("Doe")
                        .build())
                );

        // ACT & ASSERT
        assertThrows(UserNotApprovedException.class, () -> userDetailsService.loadUserByUsername("jdoe"));
    }

    @Test
    void test_loadUserByUsername_notFound_exceptionThrown() {
        // ARRANGE
        String username = "jdoe";
        when(userDaoMock.findByUsername(eq(username)))
                .thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("jdoe"));
    }

}
