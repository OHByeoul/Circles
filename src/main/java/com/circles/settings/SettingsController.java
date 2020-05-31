package com.circles.settings;

import com.circles.account.AccountService;
import com.circles.account.CurrentUser;
import com.circles.domain.Account;
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
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class SettingsController {

    private final AccountService accountService;
    private final ModelMapper modelMapper;

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
    public String getTagView(@CurrentUser Account account, Model model){
        model.addAttribute(account);
        return "/settings/tag";
    }

    @PostMapping("/settings/tag/add")
    @ResponseBody
    public ResponseEntity addTag(@CurrentUser Account account, @RequestBody TagForm tagForm) {
        System.out.println("donnnnne");
        System.out.println(tagForm.toString());
        return ResponseEntity.ok().build();
    }
}
