package com.circles.settings;

import com.circles.account.AccountRepository;
import com.circles.account.AccountService;
import com.circles.domain.Account;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SettingsControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @AfterEach
    void deleteProfile(){
        accountRepository.deleteAll();
    }

    @WithAccount("byeoul")
    @DisplayName("프로필 수정 테스트 - 성공")
    @Test
    void profileUpdate() throws Exception {
        mockMvc.perform(post("/settings/profile")
                .param("introduction","자소자소")
                .with(csrf()))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(flash().attributeExists("message"))
                    .andExpect(redirectedUrl("/settings/profile"));

        Account account = accountService.findByNickname("byeoul");
        assertEquals("자소자소", account.getIntroduction());
    }

    @WithAccount("byeoul")
    @DisplayName("프로필 수정 테스트 - 실패")
    @Test
    void 프로필수정테스트실패() throws Exception{
        mockMvc.perform(post("/settings/profile")
                        .param("introduction","wkthwwkthwwkthwwkthwwkthwwkthwwkthwwkthwwkthwwkthwwkthwwkthwwkthw")
                        .with(csrf()))
                            .andExpect(status().isOk())
                            .andExpect(view().name("settings/profile"))
                            .andExpect(model().attributeExists("account"))
                            .andExpect(model().attributeExists("profile"))
                            .andExpect(model().hasErrors());

        Account account = accountService.findByNickname("byeoul");
        assertNotEquals("wkthwwkthwwkthwwkthwwkthwwkthwwkthwwkthwwkthwwkthwwkthwwkthwwkthw", account.getIntroduction());
    }
}