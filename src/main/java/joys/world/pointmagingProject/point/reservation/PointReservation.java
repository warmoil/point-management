package joys.world.pointmagingProject.point.reservation;

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
public class PointReservation extends IdEntity {

    @ManyToOne(fetch = FetchType.LAZY ,optional = false)
    @JoinColumn(nullable = false)
    PointWallet pointWallet;

    @Column(nullable = false )
    Long amount; //적립금액

    @Column(nullable = false)
    LocalDate earnedDate;

    @Column(nullable = false)
    int availableDay;

    @Column(length = 1)
    @Setter
    boolean executed; // 사용 여부

    public PointReservation(
            PointWallet pointWallet ,
            Long amount ,
            LocalDate earnedDate,
            int availableDay
    ){
        this.pointWallet = pointWallet;
        this.amount = amount;
        this.earnedDate = earnedDate;
        this.availableDay = availableDay;
        this.executed = false;
    }

    public LocalDate getExpireDate() {
        return this.earnedDate.plusDays(this.availableDay);
    }

}
