package com.circles.main;

import com.circles.account.AccountRepository;
import com.circles.account.AccountService;
import com.circles.account.SignUpForm;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MainControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    AccountService accountService;
    @Autowired
    AccountRepository accountRepository;

    @BeforeEach
    void createAccount(){
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setNickname("byeoul");
        signUpForm.setEmail("fuck@gmail.com");
        signUpForm.setPassword("fuckk");
        accountService.createNewAccount(signUpForm);
    }

    @AfterEach
    void deleteAccount(){
        accountRepository.deleteAll();
    }

    @DisplayName("이메일로그인성공시리다이렉션테스트")
    @Test
    void 이메일로그인성공시리다이렉션테스트() throws Exception {
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setNickname("byeoul");
        signUpForm.setEmail("fuck@gmail.com");
        signUpForm.setPassword("fuckk");
        accountService.createNewAccount(signUpForm);

        mockMvc.perform(post("/login")
                .param("username","fuck@gmail.com")
                .param("password","fuckk")
                .with(csrf()))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/"))
                    .andExpect(authenticated().withUsername("byeoul"));
    }

    @DisplayName("닉네임로그인성공리다이렉션테스트")
    @Test
    void 닉네임로그인성공시리다이렉션테스트() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username","byeoul")
                        .param("password","fuckk").with(csrf()))
                            .andExpect(status().is3xxRedirection())
                            .andExpect(redirectedUrl("/"))
                            .andExpect(authenticated().withUsername("byeoul"));

    }

}