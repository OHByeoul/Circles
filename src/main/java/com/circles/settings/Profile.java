package com.circles.settings;

import com.circles.domain.Account;
import lombok.Data;

@Data
public class Profile {
    private String introduction;

    private String url;

    private String occupation;

    private String location; //사는 지역

    public Profile(Account account) {
        this.introduction = account.getIntroduction();
        this.url = account.getUrl();
        this.occupation = account.getOccupation();
        this.location = account.getLocation();
    }
}
