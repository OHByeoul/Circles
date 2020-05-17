package com.circles.settings;

import com.circles.account.AccountService;
import com.circles.account.CurrentUser;
import com.circles.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class SettingsController {
    private final AccountService accountService;

    @GetMapping("/settings/profile")
    public String profileUpdate(@CurrentUser Account account, Model model){
        model.addAttribute(account);
        model.addAttribute(new Profile(account));
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
}
