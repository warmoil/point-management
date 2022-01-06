package joys.world.pointmagingProject.point.point;

import joys.world.pointmagingProject.point.ExpiredPointSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface PointCustomRepository {
    Page<ExpiredPointSummary> sumByExpiredDate(LocalDate alarmCriteriaDate, Pageable pageable);


    Page<ExpiredPointSummary> sumBeforeCriteriaDate(LocalDate alarmCriteriaDate, Pageable pageable);


}
