package com.circles.settings;

import com.circles.account.AccountRepository;
import com.circles.account.AccountService;
import com.circles.domain.Account;
import com.circles.domain.Tag;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Autowired
    TagRepository tagRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ObjectMapper objectMapper;

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

    @DisplayName("프로필 view리턴")
    @WithAccount("byeoul")
    @Test
    void 프로필뷰리턴() throws Exception {
        mockMvc.perform(get("/settings/profile")
                    .with(csrf())) // form에 담은값 post로 올리는게 아니라 필요 없을듯
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("account"))
                    .andExpect(model().attributeExists("profile"))
                    .andExpect(view().name("settings/profile"));

    }


    @DisplayName("비밀번호 수정 화면")
    @WithAccount("byeoul")
    @Test
    void 비밀번호수정화면리턴() throws Exception {
        mockMvc.perform(get("/settings/password")
                    .with(csrf()))
                        .andExpect(status().isOk())
                        .andExpect(model().attributeExists("password"))
                        .andExpect(model().attributeExists("account"))
                        .andExpect(view().name("settings/password"));
    }

    @DisplayName("비밀번호 수정 성공")
    @WithAccount("byeoul")
    @Test
    void 비밀번호수정성공() throws Exception {
        mockMvc.perform(post("/settings/password")
                        .param("password","123456")
                        .param("passwordConfirm","123456")
                        .with(csrf()))
                            .andExpect(status().is3xxRedirection())
                            .andExpect(flash().attributeExists("message"))
                            .andExpect(redirectedUrl("/settings/password"));

        Account account = accountRepository.findByNickname("byeoul");
        assertTrue(passwordEncoder.matches("123456",account.getPassword()));
    }

    @DisplayName("비밀번호 수정 실패")
    @WithAccount("byeoul")
    @Test
    void 비밀번호수정실패() throws Exception {
        mockMvc.perform(post("/settings/password")
                        .param("password","123456")
                        .param("passwordConfirm","1234567")
                        .with(csrf()))
                            .andExpect(status().isOk())
                            .andExpect(model().attributeExists("account"))
                            .andExpect(model().hasErrors())
                            .andExpect(view().name("settings/password"));
    }

    @DisplayName("닉네임수정화면나오는지 테스트")
    @WithAccount("byeoul")
    @Test
    void 닉네임수정폼성공() throws Exception {
        mockMvc.perform(get("/settings/account"))
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("account"))
                    .andExpect(model().attributeExists("accountForm"))
                    .andExpect(view().name("settings/account"));
    }

    @DisplayName("닉네임수정기능성공")
    @WithAccount("byeoul")
    @Test
    void 닉네임수정기능성공() throws Exception {
        mockMvc.perform(post("/settings/account")
                    .param("nickname","realnickkk")
                    .with(csrf()))
                        .andExpect(status().is3xxRedirection())
                        .andExpect(redirectedUrl("/settings/account"));

        Account account = accountRepository.findByNickname("realnickkk");
        assertNotNull(account);
    }

    @DisplayName("태그조회기능")
    @WithAccount("byeoul")
    @Test
    void 태그조회기능() throws  Exception{
        mockMvc.perform(get("/settings/tag"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("whitelist"))
                .andExpect(model().attributeExists("tagList"))
                .andExpect(model().attributeExists("account"))
                .andExpect(view().name("/settings/tag"));
    }

    @DisplayName("태그추가기능")
    @WithAccount("byeoul")
    @Test
    void 태그추가기능() throws  Exception{
        TagForm tagForm = new TagForm();
        tagForm.setTagName("addTag");

        mockMvc.perform(post("/settings/tag/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(tagForm))
                    .with(csrf()))
                    .andExpect(status().isOk());

        Tag newTag = tagRepository.findByTitle("newTag");
        assertNotNull(newTag);
        Account my = accountRepository.findByNickname("byeoul");
        assertTrue(my.getTags().contains(newTag));
    }

    @DisplayName("태그삭제기능")
    @WithAccount("byeoul")
    @Test
    void 태그삭제기능() throws  Exception{
        Account my = accountRepository.findByNickname("byeoul");
        Tag tag = tagRepository.save(Tag.builder().title("newTag").build());
        accountService.addTag(my,tag);

        assertTrue(my.getTags().contains(tag));

        TagForm tagForm = new TagForm();
        tagForm.setTagName("addTag");

        mockMvc.perform(post("/settings/tag/remove")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString("newTag"))
                    .with(csrf()))
                    .andExpect(status().isOk());

        assertFalse(my.getTags().contains(tag));
    }
}
