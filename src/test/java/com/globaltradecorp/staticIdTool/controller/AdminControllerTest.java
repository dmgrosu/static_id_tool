package com.globaltradecorp.staticIdTool.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.globaltradecorp.staticIdTool.model.dto.AdminAction;
import com.globaltradecorp.staticIdTool.model.dto.AdminActionRequestDto;
import com.globaltradecorp.staticIdTool.service.AppUserService;
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

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @MockBean
    private AppUserService userServiceMock;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule())
                .disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    }

    @Test
    @WithMockUser()
    void test_adminAction_approveUser_responseIsOk() throws Exception {
        // ARRANGE
        OffsetDateTime now = OffsetDateTime.now();
        String jsonString = objectMapper.writeValueAsString(AdminActionRequestDto.builder()
                .action(AdminAction.APPROVE)
                .userId(12)
                .userTime(now)
                .build());
        // ACT
        mockMvc.perform(post("/admin/admin/action")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString))
                .andExpect(status().isOk());
        // ASSERT
        verify(userServiceMock, times(1)).approveUser(eq(12), eq(now));
    }

    @Test
    @WithMockUser()
    void test_adminAction_deleteUser_responseIsOk() throws Exception {
        // ARRANGE
        OffsetDateTime now = OffsetDateTime.now();
        String jsonString = objectMapper.writeValueAsString(AdminActionRequestDto.builder()
                .action(AdminAction.DELETE)
                .userId(12)
                .userTime(now)
                .build());
        // ACT
        mockMvc.perform(post("/admin/admin/action")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString))
                .andExpect(status().isOk());
        // ASSERT
        verify(userServiceMock, times(1)).deleteUser(eq(12), eq(now));
    }

    @Test
    @WithMockUser()
    void test_adminAction_blockUser_responseIsOk() throws Exception {
        // ARRANGE
        OffsetDateTime now = OffsetDateTime.now();
        String jsonString = objectMapper.writeValueAsString(AdminActionRequestDto.builder()
                .action(AdminAction.BLOCK)
                .userId(12)
                .userTime(now)
                .build());
        // ACT
        mockMvc.perform(post("/admin/admin/action")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString))
                .andExpect(status().isOk());
        // ASSERT
        verify(userServiceMock, times(1)).blockUser(eq(12));
    }

    @Test
    @WithMockUser()
    void test_getAllUsers_responseIsOk() throws Exception {
        // ACT
        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isOk());
        // ASSERT
        verify(userServiceMock, times(1)).getAllUsers();
    }

}
