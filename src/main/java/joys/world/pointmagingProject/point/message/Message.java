package joys.world.pointmagingProject.point.message;

import joys.world.pointmagingProject.point.IdEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Message extends IdEntity {

    @Column(nullable = false)
    String userId;

    @Column(nullable = false)
    String title;

    @Column(nullable = false, columnDefinition = "text")
    String content;

    public static Message expiredPointMessageInstance(
            String userId , LocalDate expiredDate , Long expiredAmount
    ) {
        return new Message(
                userId,
                String.format("%s 포인트 만료", expiredAmount.toString()),
                String.format("%s 기준 %s 포인트가 만료되었습니다.", expiredDate.format(DateTimeFormatter.ISO_DATE), expiredAmount)
        );
    }

    public static Message expiredSoonPointMessageInstance(
            String userId , LocalDate expiredDate , Long expiredAmount
    ) {
        return new Message(
                userId,
                String.format("%s 포인트 만료 예정", expiredAmount.toString()),
                String.format("%s 까지 %s 포인트가 만료예정입니다.", expiredDate.format(DateTimeFormatter.ISO_DATE), expiredAmount)
        );
    }
}
