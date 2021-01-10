package com.globaltradecorp.staticIdTool.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.globaltradecorp.staticIdTool.model.AppUser;
import com.globaltradecorp.staticIdTool.model.ComponentType;
import com.globaltradecorp.staticIdTool.model.StaticId;
import com.globaltradecorp.staticIdTool.model.dto.DeleteRequestDto;
import com.globaltradecorp.staticIdTool.model.dto.StaticIdDto;
import com.globaltradecorp.staticIdTool.service.StaticIdService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AjaxControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @MockBean
    private StaticIdService staticIdServiceMock;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @WithMockUser()
    void test_showExisting_validJsonArrayReturned() throws Exception {
        // ARRANGE
        when(staticIdServiceMock.getStaticIdList(eq(1), eq(""), eq(10)))
                .thenReturn(Collections.singletonList(StaticId.builder()
                        .componentType(ComponentType.builder()
                                .build())
                        .id(1L)
                        .value("someIdValue")
                        .createdBy(AppUser.builder()
                                .username("user")
                                .email("email")
                                .build())
                        .build()));
        // ACT & ASSERT
        mockMvc.perform(get("/getExisting")
                .param("componentId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].value").value("someIdValue"))
                .andExpect(jsonPath("$[0].createdBy.username").value("user"))
                .andExpect(jsonPath("$[0].createdBy.email").value("email"));
    }

    @Test
    @WithMockUser
    void test_showExisting_noComponentId_badRequestReturned() throws Exception {
        // ARRANGE
        when(staticIdServiceMock.getStaticIdList(eq(1), eq(""), eq(10)))
                .thenReturn(Collections.singletonList(StaticId.builder()
                        .componentType(ComponentType.builder()
                                .build())
                        .id(1L)
                        .value("someIdValue")
                        .createdBy(AppUser.builder()
                                .username("user")
                                .email("email")
                                .build())
                        .build()));
        // ACT & ASSERT
        mockMvc.perform(get("/getExisting")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void test_addNewIdValue_validId_serviceMethodCalled() throws Exception {
        // ARRANGE
        String jsonString = objectMapper.writeValueAsString(StaticIdDto.builder()
                .idValue("someIdValue")
                .componentId(1)
                .build());
        // ACT
        mockMvc.perform(post("/addNew")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString))
                .andExpect(status().isOk());
        // ASSERT
        verify(staticIdServiceMock, times(1)).addNewIdValue(eq("someIdValue"), eq(1));
    }

    @Test
    @WithMockUser
    void test_deleteIdValues_validIdList_serviceMethodCalled() throws Exception {
        // ARRANGE
        OffsetDateTime now = OffsetDateTime.now();
        String jsonString = objectMapper.writeValueAsString(new DeleteRequestDto(Arrays.asList(1, 2, 3), now));
        // ACT
        mockMvc.perform(post("/deleteIds")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString))
                .andExpect(status().isOk());
        // ASSERT
        // TODO: verify OffsetDateTime serialisation
        verify(staticIdServiceMock, times(1)).deleteIds(eq(Arrays.asList(1, 2, 3)), any());
    }
}
