package joys.world.pointmagingProject.job.expire;

import joys.world.pointmagingProject.BatchTestSupport;
import joys.world.pointmagingProject.point.point.Point;
import joys.world.pointmagingProject.point.point.PointRepository;
import joys.world.pointmagingProject.point.wellet.PointWallet;
import joys.world.pointmagingProject.point.wellet.PointWalletRepository;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.*;


class ExpirePointJobConfigTest extends BatchTestSupport {

    @Autowired
    PointWalletRepository pointWalletRepository;

    @Autowired
    PointRepository pointRepository;

    @Autowired
    Job expirePointJob;

    @Test
    void expirePointJob() throws Exception {
        //g
        LocalDate earnDate = LocalDate.of(2021, 1, 1);
        LocalDate expireDate = LocalDate.of(2021, 1, 3);

        PointWallet pointWallet = pointWalletRepository.save(
                new PointWallet(
                        "user12334",
                        6000L
                )
        );
        pointRepository.save(new Point(pointWallet, 1000L, earnDate, expireDate));
        pointRepository.save(new Point(pointWallet, 1000L, earnDate, expireDate));
        pointRepository.save(new Point(pointWallet, 1000L, earnDate, expireDate));

        //w
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("today", "2021-01-04")
                .toJobParameters();
        JobExecution jobExecution  =  launchJob(expirePointJob, jobParameters);

        //t

        then(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        List<Point> points = pointRepository.findAll();
        then(points.stream().filter(Point::isExpired)).hasSize(3);
        PointWallet changedPointWallet = pointWalletRepository.findById(pointWallet.getId()).orElseGet(null);
        then(changedPointWallet).isNotNull();
        then(changedPointWallet.getAmount()).isEqualByComparingTo(3000L);

    }

}
