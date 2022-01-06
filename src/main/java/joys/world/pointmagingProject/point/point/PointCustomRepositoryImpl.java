package joys.world.pointmagingProject.point.point;

import com.querydsl.jpa.JPQLQuery;
import joys.world.pointmagingProject.point.ExpiredPointSummary;
import joys.world.pointmagingProject.point.QExpiredPointSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.time.LocalDate;
import java.util.List;

public class PointCustomRepositoryImpl extends QuerydslRepositorySupport implements PointCustomRepository {

    public PointCustomRepositoryImpl() {
        super(Point.class);
    }

    @Override
    public Page<ExpiredPointSummary> sumByExpiredDate(LocalDate alarmCriteriaDate, Pageable pageable) {
        JPQLQuery<ExpiredPointSummary> query =
                from(QPoint.point)
                        .select(
                                new QExpiredPointSummary(
                                        QPoint.point.pointWallet.userId,
                                        QPoint.point.amount.sum().coalesce(0L)
                                )
                        )
                        .where(QPoint.point.isExpired.eq(true))
                        .where(QPoint.point.isUsed.eq(false))
                        .where(QPoint.point.expireDate.eq(alarmCriteriaDate))
                        .groupBy(QPoint.point.pointWallet);
        List<ExpiredPointSummary> expiredPointSummaryList = getQuerydsl().applyPagination(pageable, query).fetch();
        long elementCount = query.fetchCount();
        return new PageImpl<>(
                expiredPointSummaryList,
                PageRequest.of(pageable.getPageNumber() , pageable.getPageSize()),
                elementCount
        );
    }

    @Override
    public Page<ExpiredPointSummary> sumBeforeCriteriaDate(LocalDate alarmCriteriaDate, Pageable pageable) {
        JPQLQuery<ExpiredPointSummary> query =
                from(QPoint.point)
                        .select(
                                new QExpiredPointSummary(
                                        QPoint.point.pointWallet.userId,
                                        QPoint.point.amount.sum().coalesce(0L)
                                )
                        )
                        .where(QPoint.point.isExpired.eq(false))
                        .where(QPoint.point.isUsed.eq(false))
                        .where(QPoint.point.expireDate.lt(alarmCriteriaDate))
                        .groupBy(QPoint.point.pointWallet);

        List<ExpiredPointSummary> expiredPointSummaryList = getQuerydsl().applyPagination(pageable, query).fetch();
        long elementCount = query.fetchCount();
        return new PageImpl<>(
                expiredPointSummaryList,
                PageRequest.of(pageable.getPageNumber() , pageable.getPageSize()),
                elementCount
        );
    }
}
