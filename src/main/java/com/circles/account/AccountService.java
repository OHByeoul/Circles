package com.circles.account;

import com.circles.domain.Account;
import com.circles.settings.Password;
import com.circles.settings.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.validation.Valid;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {
    private final AccountRepository accountRepository;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Account createNewAccount(SignUpForm signUpForm) {
        Account createdAccount = createAccount(signUpForm);
        createdAccount.generateEmailCheckToken();
        sendSignUpConfirmEmail(createdAccount);
        return createdAccount;
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


    public void sendSignUpConfirmEmail(Account createdAccount) {
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

    public void login(Account account) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new UserAccount(account),
                account.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        SecurityContextHolder.getContext().setAuthentication(token);
    }

    @Transactional(readOnly = true) //데이터 변경없이 읽기만하는 것이니 readOnly옵션
    @Override
    public UserDetails loadUserByUsername(String emailOrNickname) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(emailOrNickname);
        if(account == null){
           account = accountRepository.findByNickname(emailOrNickname);
           if(account == null){
               throw new UsernameNotFoundException(emailOrNickname);
           }
        }

        return new UserAccount(account);
    }

    public Account findByNickname(String nickname) {
        return accountRepository.findByNickname(nickname);
    }

    public void completeSignUp(Account account) {
        account.initSignUpSetting();
        login(account);
    }

    public void updateProfile(Account account, Profile profile) {
        account.setIntroduction(profile.getIntroduction());
        account.setLocation(profile.getLocation());
        account.setOccupation(profile.getOccupation());
        account.setUrl(profile.getUrl());
        account.setProfileImage(profile.getProfileImage());
        accountRepository.save(account);
    }

    public void updatePassword(Account account, Password password) {
        account.setPassword(passwordEncoder.encode(password.getPassword()));
        accountRepository.save(account);
    }
}
