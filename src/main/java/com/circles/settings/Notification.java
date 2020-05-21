package com.circles.settings;

import com.circles.domain.Account;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Notification {
    private boolean circleCreatedByEmail;

    private boolean circleCreatedByWeb;

    private boolean circleEnrollmentResultByEmail;

    private boolean circleEnrollmentResultByWeb;

    private boolean circleUpdatedResultByEmail;

    private boolean circleUpdatedResultByWeb;

//    public Notification(Account account) {
//        this.circleCreatedByEmail = account.isCircleCreatedByEmail();
//        this.circleCreatedByWeb = account.isCircleCreatedByWeb();
//        this.circleEnrollmentResultByEmail = account.isCircleEnrollmentResultByEmail();
//        this.circleEnrollmentResultByWeb = account.isCircleEnrollmentResultByWeb();
//        this.circleUpdatedResultByEmail = account.isCircleUpdatedResultByEmail();
//        this.circleUpdatedResultByWeb = account.isCircleUpdatedResultByWeb();
//    }
}
