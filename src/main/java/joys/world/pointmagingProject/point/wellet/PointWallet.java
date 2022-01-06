package joys.world.pointmagingProject.point.wellet;

import joys.world.pointmagingProject.point.IdEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class PointWallet extends IdEntity {


    @Column(unique = true , nullable = false)
    String userId;

    @Setter
    Long amount;



}
