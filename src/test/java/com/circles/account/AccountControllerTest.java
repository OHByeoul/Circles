package com.circles.account;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @DisplayName("회원 가입 화면 노출 여부 테스트")
    @Test
    public void signUpForm() throws Exception {
        mockMvc.perform(get("/sign-up"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"))
                .andExpect(model().attributeExists("signUpForm"));
    }

    @DisplayName("회원 가입 버튼클릭시 - 잘못된 입력값")
    @Test
    public void 회원가입버튼클릭시잘못된입력값() throws Exception{
        mockMvc.perform(post("/sign-up")
                .param("nickname","nick")
                .param("email","isnotemailform")
                .param("password","12345")
                .with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(view().name("account/sign-up"));

    }

    @DisplayName("회원 가입 버튼클릭시 - 정확한 입력값")
    @Test
    public void 회원가입버튼클릭시정확한입력값() throws Exception{
        mockMvc.perform(post("/sign-up")
                .param("nickname","nickname")
                .param("email","isemail@gmail.com")
                .param("password","123456")
                .with(csrf()))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(view().name("redirect:/"));

        assertTrue(accountRepository.existsByEmail("isemail@gmail.com"));

    }

}
