package com.globaltradecorp.staticIdTool.service;

import com.globaltradecorp.staticIdTool.dao.StaticIdDao;
import com.globaltradecorp.staticIdTool.model.AppUser;
import com.globaltradecorp.staticIdTool.model.ComponentType;
import com.globaltradecorp.staticIdTool.model.StaticId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
class StaticIdServiceTest {

    @Mock
    private StaticIdDao staticIdDaoMock;
    @Mock
    private AppUserService appUserServiceMock;
    @InjectMocks
    private StaticIdService staticIdService;

    @AfterEach
    void tearDown() {
        reset(staticIdDaoMock, appUserServiceMock);
    }

    @Test
    void test_getComponentList_daoCalledOnce() {
        // ACT
        staticIdService.getComponentList();
        // ASSERT
        verify(staticIdDaoMock, times(1)).getComponentList();
    }

    @Test
    void test_getStaticIdList_daoCalledOnce() {
        // ACT
        staticIdService.getStaticIdList(1, "L", 10);
        // ASSERT
        verify(staticIdDaoMock, times(1)).getStaticIdList(eq(1), eq("L"), eq(10));
    }

    @Test
    void test_addNewIdValue_nullValue_exceptionThrown() {
        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class, () -> staticIdService.addNewIdValue(null, 1));
    }

    @Test
    void test_addNewIdValue_nullComponent_exceptionThrown() {
        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class, () -> staticIdService.addNewIdValue(Collections.singletonList("someValue"), null));
    }

    @Test
    void test_addNewIdValue_existingValue_exceptionThrown() {
        // ARRANGE
        when(staticIdDaoMock.idValueExists(anyList())).thenReturn(true);
        // ACT & ASSERT
        assertThrows(IdValueExistsException.class, () -> staticIdService.addNewIdValue(Collections.singletonList("someValue"), 1));
    }

    @Test
    void test_addNewIdValue_currentUserNotFound_exceptionThrown() {
        // ARRANGE
        when(staticIdDaoMock.idValueExists(anyList())).thenReturn(false);
        when(appUserServiceMock.getCurrentUser()).thenReturn(null);
        // ACT & ASSERT
        assertThrows(RuntimeException.class, () -> staticIdService.addNewIdValue(Collections.singletonList("someValue"), 1));
    }

    @Test
    void test_addNewIdValue_daoCalledOnce() throws IdValueExistsException {
        // ARRANGE
        when(staticIdDaoMock.idValueExists(anyList())).thenReturn(false);
        when(appUserServiceMock.getCurrentUser())
                .thenReturn(AppUser.builder()
                        .id(1)
                        .username("user")
                        .firstName("John")
                        .lastName("Doe")
                        .email("someEmail")
                        .build());
        when(staticIdDaoMock.getComponentTypeById(anyInt()))
                .thenReturn(ComponentType.builder()
                        .id(1)
                        .name("someComponent")
                        .build());
        ArgumentCaptor<StaticId> captor = ArgumentCaptor.forClass(StaticId.class);
        // ACT
        staticIdService.addNewIdValue(Collections.singletonList("someNewValue"), 1);
        // ASSERT
        verify(staticIdDaoMock, times(1)).saveStaticId(captor.capture());
        StaticId actualId = captor.getValue();
        AppUser actualUser = actualId.getCreatedBy();
        ComponentType actualComponent = actualId.getComponentType();
        assertEquals("someNewValue", actualId.getValue());
        assertEquals("user", actualUser.getUsername());
        assertEquals("John", actualUser.getFirstName());
        assertEquals("Doe", actualUser.getLastName());
        assertEquals("someEmail", actualUser.getEmail());
        assertEquals(1, actualComponent.getId());
        assertEquals("someComponent", actualComponent.getName());
    }
}
