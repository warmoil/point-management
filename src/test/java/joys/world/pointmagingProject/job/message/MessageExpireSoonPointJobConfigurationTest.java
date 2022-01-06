package joys.world.pointmagingProject.job.message;

import joys.world.pointmagingProject.BatchTestSupport;
import joys.world.pointmagingProject.point.message.Message;
import joys.world.pointmagingProject.point.message.MessageRepository;
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

class MessageExpireSoonPointJobConfigurationTest extends BatchTestSupport {

    @Autowired
    Job messageExpireSoonPointJob;
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    PointWalletRepository walletRepository;
    @Autowired
    PointRepository pointRepository;

    @Test
    void messageExpiredJob() throws Exception {

        //g
        //포인트 지갑을 생성
        //오늘 만료시킨 포인트 적립 내역을 생성(만료일자 =어제)

        LocalDate earnDate = LocalDate.of(2021, 1, 6);
        LocalDate expiredDate = LocalDate.of(2021, 1, 5);
        LocalDate notExpiredDate = LocalDate.of(2024, 1, 4);

        PointWallet wallet1 = walletRepository.save(new PointWallet("u1", 3000L));
        PointWallet wallet2 = walletRepository.save(new PointWallet("u2", 0L));

        pointRepository.save(new Point(wallet1, 1000L, earnDate, expiredDate));
        pointRepository.save(new Point(wallet1, 1000L, earnDate, expiredDate));
        pointRepository.save(new Point(wallet1, 1000L, earnDate, expiredDate));

        pointRepository.save(new Point(wallet2, 1000L, earnDate, expiredDate));
        pointRepository.save(new Point(wallet2, 1000L, earnDate, notExpiredDate));
        pointRepository.save(new Point(wallet2, 1000L, earnDate, notExpiredDate));

        //w(실행)
        //messageExpiredJob 실행
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("today", "2021-09-06")
                .toJobParameters();
        JobExecution execution = launchJob(messageExpireSoonPointJob, jobParameters);

        //t
        //아래와 같은 메세지가 생성되었는지 확인
        //xxx 포인트 만료
        //yyyy-MM-dd 기준 xxx 포인트가 만료되었습니다
        //각 사용자마다 몇포인트가 만료됐는지 메시지
        then(execution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        List<Message> messages = messageRepository.findAll();
        then(messages).hasSize(2);
        Message message1 = messages.stream().filter(item -> item.getUserId().equals("u1")).findFirst().orElseGet(null);
        then(message1).isNotNull();
        then(message1.getTitle()).isEqualTo("3000 포인트 만료 예정");
        then(message1.getContent()).isEqualTo("2021-09-06 까지 3000 포인트가 만료예정입니다.");

        Message message2 = messages.stream().filter(item -> item.getUserId().equals("u2")).findFirst().orElseGet(null);
        then(message2).isNotNull();
        then(message2.getTitle()).isEqualTo("1000 포인트 만료 예정");
        then(message2.getContent()).isEqualTo("2021-09-06 까지 1000 포인트가 만료예정입니다.");

    }
}