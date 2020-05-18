package com.circles.settings;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PasswordValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return Password.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Password password = (Password) target;
        if(!password.getPassword().equals(password.getPasswordConfirm())){
            errors.rejectValue("password","invalid value","비밀번호가 서로 다름");
        }
    }
}
