package com.circles.settings;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class AccountForm {
    @Length(min=5,max=15)
    private String nickname;
}
