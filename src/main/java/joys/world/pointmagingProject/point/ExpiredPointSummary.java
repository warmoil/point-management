package joys.world.pointmagingProject.point;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class ExpiredPointSummary {
    String userId;
    Long amount; //만료금액

    @QueryProjection
    public ExpiredPointSummary(
            String userId ,
            Long amount
    ) {
        this.userId = userId;
        this.amount = amount;
    }
}
