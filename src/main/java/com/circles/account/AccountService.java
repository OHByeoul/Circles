package com.circles.account;

import com.circles.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.validation.Valid;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void createNewAccount(SignUpForm signUpForm) {
        Account createdAccount = createAccount(signUpForm);
        createdAccount.generateEmailCheckToken();
        sendSignUpConfirmEmail(createdAccount);
    }

    private Account createAccount(@ModelAttribute @Valid SignUpForm signUpForm) {
        Account newAccount = Account.builder()
                    .email(signUpForm.getEmail())
                    .nickname(signUpForm.getNickname())
                    .password(passwordEncoder.encode(signUpForm.getPassword()))
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
        simpleMailMessage.setText("/check-email-token?token="+createdAccount.getEmailCheckToken()+"&email="+createdAccount.getEmail());
        javaMailSender.send(simpleMailMessage);
    }

    public Account findByEmail(String email) {
         return accountRepository.findByEmail(email);
    }

    public Object getThisUserNumber() {
        return accountRepository.count();
    }
}
