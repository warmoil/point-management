package joys.world.pointmagingProject.point.point;


import joys.world.pointmagingProject.point.IdEntity;
import joys.world.pointmagingProject.point.wellet.PointWallet;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Point extends IdEntity {


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "point_wallet_id", nullable = false)
    PointWallet pointWallet;

    @Column(nullable = false)
    Long amount;

    @Column(nullable = false)
    LocalDate earnedDate;

    @Column(nullable = false)
    LocalDate expireDate;
    //사용했는지
    @Column(nullable = false)
    boolean isUsed;
    //만료됐는지
    @Setter
    @Column(nullable = false)
    boolean isExpired;

    public Point(
            PointWallet wallet,
            Long amount,
            LocalDate earnedDate,
            LocalDate expireDate
    ) {
        this.pointWallet = wallet;
        this.amount = amount;
        this.earnedDate = earnedDate;
        this.expireDate = expireDate;
        this.isUsed = false;

    }

    public void expire() {
        if (!this.isUsed) {
            this.isExpired = true;
        }
    }

}
