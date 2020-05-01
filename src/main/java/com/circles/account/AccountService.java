package com.circles.account;

import com.circles.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.validation.Valid;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final JavaMailSender javaMailSender;

    public void createNewAccount(SignUpForm signUpForm) {
        Account createdAccount = createAccount(signUpForm);
        createdAccount.generateEmailCheckToken();
        sendSignUpConfirmEmail(createdAccount);
    }

    private Account createAccount(@ModelAttribute @Valid SignUpForm signUpForm) {
        Account newAccount = Account.builder()
                    .email(signUpForm.getEmail())
                    .nickname(signUpForm.getNickname())
                    .password(signUpForm.getPassword())
                    .emailVerified(false)
                    .circleCreatedByEmail(true)
                    .circleCreatedByWeb(true)
                    .circleUpdatedResultByWeb(true)
                    .build();

        return accountRepository.save(newAccount);
    }


    private void sendSignUpConfirmEmail(Account createdAccount) {
    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
    simpleMailMessage.setSubject("씨씨 써클가입 인증메일입니다.");
    simpleMailMessage.setTo(createdAccount.getEmail());
    simpleMailMessage.setText("/check-token?token="+createdAccount.getEmailCheckToken()+"&email="+createdAccount.getEmail());
    javaMailSender.send(simpleMailMessage);
}
}
