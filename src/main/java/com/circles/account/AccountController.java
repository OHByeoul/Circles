package com.circles.account;

import com.circles.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

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

        Account account = accountService.createNewAccount(signUpForm);
        accountService.login(account);
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

        if(!account.isValidToken(token)){
            model.addAttribute("errorMsg","잘못된 토큰입니다");
            return view;
        }

        account.initValueSetting();
        model.addAttribute("createdUserNumber", accountService.getThisUserNumber());
        model.addAttribute("createdUserNickname",account.getNickname());
        return view;
    }

    @GetMapping("/check-email")
    public String checkEmail(@CurrentUser Account account, Model model){
        model.addAttribute("email",account.getEmail());
        return "account/checkEmail-confirm";
    }

    @GetMapping("/resend-email-token")
    public String resendEmailToken(@CurrentUser Account account, Model model){
        if(!account.isResendConfirmEmail()){
            model.addAttribute("errorMsg","인증 이메일은 2시간에 한번 전송가능합니다.");
            return "account/checkEmail";
        }
        accountService.sendSignUpConfirmEmail(account);
       // model.addAttribute("emailSend", true);
        return "redirect:/"; //해당 url로 요청계속갈까봐 redirect시킨다
    }

    private boolean isIncorrectToken(String myToken, String otherToken){
        return !myToken.equals(otherToken);
    }



}
