package com.circles.account;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class AccountController {

    @GetMapping("/sign-up")
    public String signUpForm(Model model){
        model.addAttribute("signUpForm", new SignUpForm());
        return "account/sign-up";
    }

    @PostMapping("sign-up")
    public String signUpSubmit(@Valid @ModelAttribute SignUpForm signUpForm, Errors errors){ //파라미터로 쓰일때 @ModelAttribute 생략가능
        if(errors.hasErrors()){
            return "account/sign-up";
        }

        return "redirect:/";
    }
}
