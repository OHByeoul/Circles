package com.circles.settings;

import com.circles.account.AccountService;
import com.circles.account.CurrentUser;
import com.circles.domain.Account;
import com.circles.domain.Tag;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class SettingsController {

    private final AccountService accountService;
    private final TagService tagService;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;

    @InitBinder("password")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(new PasswordValidator());
    }

    @GetMapping("/settings/profile")
    public String profileUpdate(@CurrentUser Account account, Model model){
        model.addAttribute(account);
        model.addAttribute(modelMapper.map(account,Profile.class));
        return "settings/profile";
    }

    @PostMapping("/settings/profile")
    public String profileUpdate(@CurrentUser Account account, @Valid @ModelAttribute Profile profile,
                                Errors errors, Model model, RedirectAttributes attributes){
        if(errors.hasErrors()){
            model.addAttribute(account); // error발생시 에러정보는 자동으로 붙여준다.
            return "settings/profile";
        }
        accountService.updateProfile(account, profile);
        attributes.addFlashAttribute("message","수정이 됫음");
        return "redirect:/settings/profile";
    }

    @GetMapping("/settings/password")
    public String passwordUpdate(@CurrentUser Account account, Model model){
        model.addAttribute(account);
        model.addAttribute("password", new Password());
        return "settings/password";
    }

    @PostMapping("/settings/password")
    public String passwordUpdate(@CurrentUser Account account,@Valid @ModelAttribute Password password,
                                 Errors errors, Model model, RedirectAttributes attributes){
        if(errors.hasErrors()){
            model.addAttribute(account);
            return "settings/password";
        }

        accountService.updatePassword(account,password);
        attributes.addFlashAttribute("message","비밀번호가 변경되었음");
        return "redirect:/settings/password";
    }

    @GetMapping("/settings/notification")
    public String updateNotification(@CurrentUser Account account, Model model){
        model.addAttribute(account);
        model.addAttribute(modelMapper.map(account,Notification.class));
        return "settings/notification";
    }

    @PostMapping("/settings/notification")
    public String updateNotification(@CurrentUser Account account, @Valid @ModelAttribute Notification notification
                                     ,RedirectAttributes redirectAttributes,Errors errors ,Model model){
        if(errors.hasErrors()){
            model.addAttribute(account); // 에러는 붙어서 나옴
            return "settings/notification";
        }
        accountService.updateNotification(account,notification);
        redirectAttributes.addFlashAttribute("message","알림이 수정되었음");
        return "redirect:/settings/notification";
    }

    @GetMapping("/settings/account")
    public String updateAccount(@CurrentUser Account account, Model model){
        model.addAttribute(account);
        model.addAttribute("accountForm",new AccountForm(account));
        return "settings/account";
    }

    @PostMapping("/settings/account")
    public String updateAccount(@CurrentUser Account account,@Valid @ModelAttribute AccountForm accountForm,Errors errors, Model model){
        if(errors.hasErrors()){
            model.addAttribute(account);
            return "settings/account";
        }
        accountService.updateAccount(account,accountForm);
        return "redirect:/settings/account";
    }

    @GetMapping("/settings/tag")
    public String getTagView(@CurrentUser Account account, Model model) throws JsonProcessingException {
        Set<Tag> tags = accountService.getTags(account);
        List<String> tagList = tags.stream().map(Tag::getTitle).collect(Collectors.toList());

        List<String> allTags = tagService.findAll();
        model.addAttribute("whitelist",objectMapper.writeValueAsString(allTags));
        model.addAttribute("tagList",tagList);
        model.addAttribute(account);
        return "/settings/tag";
    }

    @PostMapping("/settings/tag/add")
    @ResponseBody
    public ResponseEntity addTag(@CurrentUser Account account, @RequestBody TagForm tagForm) {
        String tagName = tagForm.getTagName();
        Tag tag = tagService.findByTitle(tagName);
        if(tag == null){
            tag = tagService.addTag(Tag.builder().title(tagForm.getTagName()).build());
        }
        accountService.addTag(account,tag);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/settings/tag/remove")
    @ResponseBody
    public ResponseEntity removeTag(@CurrentUser Account account, @RequestBody TagForm tagForm) {
        String tagName = tagForm.getTagName();
        Tag tag = tagService.findByTitle(tagName);
        if(tag != null){
            tagService.removeTag(Tag.builder().title(tagForm.getTagName()).build());
        }
        accountService.removeTag(account,tag);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/settings/zone")
    public String getZoneView(@CurrentUser Account account, Model model){
        model.addAttribute(account);
        return "/settings/zone";
    }
}
