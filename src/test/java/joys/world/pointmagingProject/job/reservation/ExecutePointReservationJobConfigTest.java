package joys.world.pointmagingProject.job.reservation;

import joys.world.pointmagingProject.BatchTestSupport;
import joys.world.pointmagingProject.point.point.Point;
import joys.world.pointmagingProject.point.point.PointRepository;
import joys.world.pointmagingProject.point.reservation.PointReservation;
import joys.world.pointmagingProject.point.reservation.PointReservationRepository;
import joys.world.pointmagingProject.point.wellet.PointWallet;
import joys.world.pointmagingProject.point.wellet.PointWalletRepository;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.*;

class ExecutePointReservationJobConfigTest extends BatchTestSupport {

    @Autowired
    PointWalletRepository walletRepository;
    @Autowired
    PointReservationRepository reservationRepository;
    @Autowired
    PointRepository pointRepository;
    @Autowired
    Job executePointReservationJob;


    /**
     * g point reservation (예약) 이 있어야함
     * w executePointReservationJob 을 실행시킵니다
     * t reservation 은 완료처리 되어야합니다
     *  point 적립이 생겨야합니다
     */
    @Test
    void executePointReservationJob() throws Exception {

        //g
        LocalDate earnDate = LocalDate.of(2021, 1, 5);

        PointWallet wallet = walletRepository.save(new PointWallet("user1", 3000L));
        reservationRepository.save(new PointReservation(wallet,1000L,earnDate,10));

        //w
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("today", "2021-01-05")
                .toJobParameters();
        JobExecution jobExecution = launchJob(executePointReservationJob, jobParameters);

        //t
        //반영 확인
        then(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        List<PointReservation> pointReservations = reservationRepository.findAll();
        then(pointReservations).hasSize(1);
        then(pointReservations.get(0).isExecuted()).isTrue();

        List<Point> points = pointRepository.findAll();
        then(points).hasSize(1);
        then(points.get(0).getAmount()).isEqualByComparingTo(1000L);
        then(points.get(0).getEarnedDate()).isEqualTo(LocalDate.of(2021, 1, 5));
        then(points.get(0).getExpireDate()).isEqualTo(LocalDate.of(2021, 1, 15));

        List<PointWallet> wallets = walletRepository.findAll();
        then(wallets).hasSize(1);
        then(wallets.get(0).getAmount()).isEqualByComparingTo(4000L);
    }
}