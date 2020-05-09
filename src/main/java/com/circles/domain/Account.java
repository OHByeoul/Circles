package com.circles.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter @Setter
@EqualsAndHashCode(of="id")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String email; // email login enable

    @Column(unique = true)
    private String nickname; //nick login enable

    private String password;

    private boolean emailVerified; // 인증됫는지

    private String emailCheckToken; // 인증토큰값

    private LocalDateTime joinedAt;

    private String introduction;

    private String url;

    private String occupation;

    private String location; //사는 지역

    @Lob @Basic(fetch = FetchType.EAGER)
    private String profileImage;

    //notification
    private boolean circleCreatedByEmail;

    private boolean circleCreatedByWeb;

    private boolean circleEnrollmentResultByEmail;

    private boolean circleEnrollmentResultByWeb;

    private boolean circleUpdatedResultByEmail;

    private boolean circleUpdatedResultByWeb;

    private LocalDateTime emailCheckTokenGenerateTime;

    public void generateEmailCheckToken() {
        this.emailCheckToken = UUID.randomUUID().toString();
        this.emailCheckTokenGenerateTime = LocalDateTime.now();
    }

    public void initValueSetting() {
        this.setEmailVerified(true);
        this.setJoinedAt(LocalDateTime.now());
    }

    public boolean isValidToken(String token) {
        return this.emailCheckToken.equals(token);
    }

    public boolean isResendConfirmEmail() {
        return this.emailCheckTokenGenerateTime.isBefore(LocalDateTime.now().minusHours(2));
    }
}
