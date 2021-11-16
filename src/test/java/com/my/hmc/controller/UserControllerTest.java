package com.my.hmc.controller;

import com.google.gson.Gson;
import com.my.hmc.domain.User;
import com.my.hmc.domain.etype.UserRole;
import com.my.hmc.request.SignupRequestDto;
import com.my.hmc.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    UserService userService;

    @Autowired
    MockMvc mockMvc;

    @Test
    void 일반_회원가입() throws Exception {

        // Given
        SignupRequestDto dto = SignupRequestDto.builder()
                .username("test")
                .password("password")
                .reviewer(false)
                .build();

        Mockito.doReturn(
                new User(1L, dto.getUsername(), dto.getPassword(), UserRole.ROLE_USER, 0, new ArrayList<>())
        ).when(userService).saveUser(dto);


        // When
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(dto))
        );


        // Then
        MvcResult mvcResult = resultActions.andExpect(status().isOk()).andReturn();
        String body = mvcResult.getResponse().getContentAsString();

        System.out.println(body);
    }
}