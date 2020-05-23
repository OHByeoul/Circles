package com.circles.settings;

import com.circles.domain.Account;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
public class AccountForm {
    @Length(min=5,max=15)
    private String nickname;

    public AccountForm(Account account) {
        this.nickname = account.getNickname();
    }
}
