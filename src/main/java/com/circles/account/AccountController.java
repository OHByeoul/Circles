package com.circles.account;

import com.circles.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class AccountController {
    private final SignUpFormValidator signUpFormValidator;
    private final AccountService accountService;


    @InitBinder("signUpFrom")
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(signUpFormValidator);
    }

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/sign-up")
    public String signUpForm(Model model){
        model.addAttribute("signUpForm", new SignUpForm());
        return "account/sign-up";
    }

    @PostMapping("/sign-up")
    public String signUpSubmit(@Valid @ModelAttribute SignUpForm signUpForm, Errors errors){ //파라미터로 쓰일때 @ModelAttribute 생략가능
        //signUpFormValidator.validate(signUpForm, errors);
        if(errors.hasErrors()){
            return "account/sign-up";
        }

        accountService.createNewAccount(signUpForm);
        return "redirect:/";
    }

    @GetMapping("/check-email-token")
    public String checkEmailToken(String token, String email, Model model){
        Account account = accountService.findByEmail(email);
        String view = "account/checkEmail";

        if(account == null){
            model.addAttribute("errorMsg","이메일이 존재하지 않습니다");
            return view;
        }

        if(isIncorrectToken(account.getEmailCheckToken(),token)){
            model.addAttribute("errorMsg","잘못된 토큰입니다");
            return view;
        }

        account.initValueSetting();
        model.addAttribute("createdUserNumber", accountService.getThisUserNumber());
        model.addAttribute("createdUserNickname",account.getNickname());
        return view;
    }

    private boolean isIncorrectToken(String myToken, String otherToken){
        return !myToken.equals(otherToken);
    }



}
