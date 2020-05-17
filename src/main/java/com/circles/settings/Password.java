package com.circles.settings;

import com.circles.domain.Account;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class Password {
    @Length(min = 6, max=20)
    private String password;

    @Length(min = 6, max=20)
    private String passwordConfirm;
}
