package com.muted987.cloudStorage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.muted987.cloudStorage.configuration.TestConfig;
import com.muted987.cloudStorage.dto.request.LoginDTO;
import com.muted987.cloudStorage.dto.request.RegisterDTO;
import com.muted987.cloudStorage.repository.UserRepository;
import com.muted987.cloudStorage.service.UserService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestConfig.class)
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    private static final String VALID_USERNAME = "username";
    private static final String NOT_VALID_USERNAME = "user";
    private static final String VALID_PASSWORD = "password";
    private static final String NOT_VALID_PASSWORD = "pass";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void clearDb() {
        this.userRepository.deleteAll();
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;


    private Cookie createTestCookie() {
        Cookie testCookie = new Cookie("testCookie", "testCookie");
        testCookie.setPath("/");
        return testCookie;
    }

    private void createUser(String username, String password) {
        userService.createUser(new RegisterDTO(username, password));
    }
    @Nested
    class SignUpTest {



        private static final String SIGN_UP_REQUEST = "/api/auth/sign-up";

        @Test
        public void testSignUp_withNotValidUsername_shouldReturn400() throws Exception {
            RegisterDTO registerDTO = new RegisterDTO(NOT_VALID_USERNAME, VALID_PASSWORD);

            mockMvc.perform(post(SIGN_UP_REQUEST)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(registerDTO))
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testSignUp_withNotValidPassword_shouldReturn400() throws Exception {


            RegisterDTO registerDTO = new RegisterDTO(VALID_USERNAME, NOT_VALID_PASSWORD);

            mockMvc.perform(post(SIGN_UP_REQUEST)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(registerDTO))
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testSignUp_withValidCredentials_shouldReturn201() throws Exception {

            RegisterDTO registerDTO = new RegisterDTO(VALID_USERNAME, VALID_PASSWORD);

            mockMvc.perform(post(SIGN_UP_REQUEST)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(registerDTO)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.username").value(VALID_USERNAME));
        }

        @Test
        void testSignUp_withNotUniqueUser_shouldReturn409() throws Exception {

            RegisterDTO registerDTO = new RegisterDTO(VALID_USERNAME, VALID_PASSWORD);

            createUser(VALID_USERNAME, VALID_PASSWORD);

            mockMvc.perform(post(SIGN_UP_REQUEST)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(registerDTO)))
                    .andDo(print())
                    .andExpect(status().isConflict());

        }

    }
    @Nested
    class SignInTest {


        private static final String SIGN_IN_REQUEST = "/api/auth/sign-in";

        @Test
        void testSignIn_withValidCredentials_shouldReturnOk() throws Exception {
            createUser(VALID_USERNAME, VALID_PASSWORD);
            LoginDTO loginDTO = new LoginDTO(VALID_USERNAME, VALID_PASSWORD);

            mockMvc.perform(post(SIGN_IN_REQUEST)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginDTO)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.username").value(VALID_USERNAME));
        }

        @Test
        void testSignIn_withNotExistingPerson_shouldReturnUnauthorized() throws Exception {
            LoginDTO loginDTO = new LoginDTO(VALID_USERNAME, VALID_PASSWORD);

            mockMvc.perform(post(SIGN_IN_REQUEST)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginDTO)))
                    .andDo(print())
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void testSignIn_withNotRightPassword_shouldReturnUnauthorized() throws Exception {
            createUser(VALID_USERNAME, VALID_PASSWORD);

            LoginDTO loginDTO = new LoginDTO(VALID_USERNAME, VALID_PASSWORD.concat("1"));

            mockMvc.perform(post(SIGN_IN_REQUEST)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginDTO)))
                    .andDo(print())
                    .andExpect(status().isUnauthorized());

        }

    }
    @Nested
    class SignOutTest {


        private static final String SIGN_OUT_REQUEST = "/api/auth/sign-out";

        @Test
        @WithAnonymousUser
        void testSignOut_withAnonymousUser_shouldReturnUnauthorized() throws Exception {

            mockMvc.perform(post(SIGN_OUT_REQUEST))
                    .andDo(print())
                    .andExpect(status().isUnauthorized());

        }

        @Test
        @WithMockUser(username = "username", roles = "USER")
        void testSignOut_withUser_shouldReturnNoContent() throws Exception {


            mockMvc.perform(post(SIGN_OUT_REQUEST)
                            .cookie(createTestCookie()))
                    .andDo(print())
                    .andExpect(status().isNoContent());

        }

    }

}