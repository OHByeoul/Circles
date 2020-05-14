package com.circles.settings;

import com.circles.domain.Account;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
public class Profile {
    @Length(max=40)
    private String introduction;

    @Length(max=67)
    private String url;

    @Length(max=40)
    private String occupation;

    @Length(max=70)
    private String location; //사는 지역

    private String profileImage;

    public Profile(Account account) {
        this.introduction = account.getIntroduction();
        this.url = account.getUrl();
        this.occupation = account.getOccupation();
        this.location = account.getLocation();
        this.profileImage = account.getProfileImage();
    }
}
