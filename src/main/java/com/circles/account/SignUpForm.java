package com.circles.account;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class SignUpForm {
    @NotBlank
    @Length(min=4, max=15)
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9_-]{4,15}$")
    private String nickname;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Length(min=6, max=20)
    private String password;
}
