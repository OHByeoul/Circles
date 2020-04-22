package com.circles.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    private String bio;

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

}
