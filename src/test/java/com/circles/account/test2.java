package com.circles.account;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;


import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class test2 {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    JavaMailSender javaMailSender;

    @Autowired
    AccountRepository accountRepository;

    @DisplayName("회원가입 잘나오는지 테스트")
    @Test
    void 회원가입잘나와() throws Exception {
        mockMvc.perform(get("/sign-up"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"))
                .andExpect(model().attributeExists("signUpForm"));
    }

    @DisplayName("회원가입 버튼 클릭시")
    @Test
    void 회원가입버튼클릭시() throws Exception {
        mockMvc.perform(post("/sign-up")
                .param("nickName","asdfff")
                .param("email","asdfffff")
                .param("password","123456").with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(view().name("account/sign-up"));
    }

    @DisplayName("회원가입 버큰 클릭 알맞은 값")
    @Test
    void 회원가입알맞은값() throws Exception {
        mockMvc.perform(post("/sign-up")
                    .param("nickname","asdffgg")
                    .param("email","asdf@gmail.com")
                    .param("password","123456").with(csrf()))
                        .andExpect(status().is3xxRedirection())
                        .andExpect(view().name("redirect:/"));

        assertTrue(accountRepository.existsByEmail("asdf@gmail.com"));
        then(javaMailSender).should().send(any(SimpleMailMessage.class));
    }
}
