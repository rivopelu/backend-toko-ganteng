package com.tokoganteng.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tokoganteng.app.TokoGantengBackendApplication;
import com.tokoganteng.app.dto.request.RequestSignIn;
import com.tokoganteng.app.dto.request.RequestSignUp;
import com.tokoganteng.app.entities.Account;
import com.tokoganteng.app.enums.AccountRoleEnum;
import com.tokoganteng.app.enums.SignUpTypeEnum;
import com.tokoganteng.app.repositories.AccountRepository;
import com.tokoganteng.app.utils.EntityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = TokoGantengBackendApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class AuthControllerTest {
    private static final String TEST_EMAIL = "testing@example.com";
    private static final String TEST_PASSWORD = "123456";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        accountRepository.deleteAll();
        saveTestAccount();
    }

    @Test
    public void testPing() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/auth/ping"))
                .andExpect(status().isOk());
    }

    @Test
    public void testSignUp() throws Exception {
        performPostRequest("/auth/v1/user/sign-up", createSignUpRequest("newuser@example.com"))
                .andExpect(status().isCreated());
    }

    @Test
    public void emailAlreadyExist() throws Exception {
        performPostRequest("/auth/v1/user/sign-up", createSignUpRequest(TEST_EMAIL))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void signInTest() throws Exception {
        performPostRequest("/auth/v1/user/sign-in", createSignInRequest(TEST_PASSWORD))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response_data.access_token").exists())
                .andExpect(jsonPath("$.response_data.access_token").isNotEmpty());
    }

    @Test
    public void signInFailed() throws Exception {
        performPostRequest("/auth/v1/user/sign-in", createSignInRequest("wrongpassword"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errors.message").value("Sign in failed"));
    }

    private void saveTestAccount() {
        Account account = Account.builder()
                .firstName("first")
                .lastName("last")
                .email(TEST_EMAIL)
                .password(passwordEncoder.encode(TEST_PASSWORD))
                .role(AccountRoleEnum.USER)
                .signUpType(SignUpTypeEnum.EMAIL)
                .phoneNumber("08080808080")
                .build();
        account.setId(UUID.randomUUID().toString());
        EntityUtils.created(account, "system");
        accountRepository.save(account);
    }

    private RequestSignUp createSignUpRequest(String email) {
        return RequestSignUp.builder()
                .firstName("test")
                .lastName("user")
                .email(email)
                .phoneNumber("8989898989")
                .password(TEST_PASSWORD)
                .build();
    }

    private RequestSignIn createSignInRequest(String password) {
        return RequestSignIn.builder()
                .email(AuthControllerTest.TEST_EMAIL)
                .password(password)
                .build();
    }

    private ResultActions performPostRequest(String url, Object request) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
    }
}