package com.my.hmc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.my.hmc.domain.User;
import com.my.hmc.domain.etype.UserRole;
import com.my.hmc.request.SigninRequestDto;
import com.my.hmc.request.SignupRequestDto;
import com.my.hmc.response.SigninResponseDto;
import com.my.hmc.security.UserDetailsServiceImpl;
import com.my.hmc.security.jwt.JwtUtils;
import com.my.hmc.service.UserService;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.HashMap;

import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(UserController.class)
@ExtendWith({MockitoExtension.class})
@Import(JwtUtils.class)
class UserControllerTest {

    @MockBean
    UserService userService;
    @MockBean
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    MockMvc mockMvc;



    @Test
    @DisplayName("일반 회원가입 테스트")
    void 일반_회원가입() throws Exception {

        // Given
        SignupRequestDto dto = SignupRequestDto.builder()
                .username("test")
                .password("password")
                .reviewer(false)
                .build();

        // When
        doReturn(new User(1L, dto.getUsername(), dto.getPassword(), UserRole.ROLE_USER, 0, new ArrayList<>()))
                .when(userService).saveUser(dto);

        Gson gson = new Gson();


        // Then
        mockMvc.perform(MockMvcRequestBuilders.post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(dto)))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.result").value("success")
                );
    }

    @DisplayName("로그인 성공")
    @Test
    void 로그인_성공() throws Exception {
        SigninRequestDto requestDto = SigninRequestDto.builder()
                .username("test").password("test").build();

        String token = jwtUtils.createToken(requestDto.getUsername());
        SigninResponseDto responseDto = SigninResponseDto.builder()
                .token(token)
                .authority("ROLE_USER")
                .httpStatus(HttpStatus.OK)
                .message("로그인에 성공했습니다.").build();

        doReturn(responseDto).when(userService).signin(requestDto);

        Gson gson = new Gson();

        mockMvc.perform(MockMvcRequestBuilders.post("/user/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(requestDto)))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.token").value(token)
                );
    }
}