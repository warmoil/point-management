package joys.world.pointmagingProject;

import joys.world.pointmagingProject.point.message.MessageRepository;
import joys.world.pointmagingProject.point.point.PointRepository;
import joys.world.pointmagingProject.point.reservation.PointReservation;
import joys.world.pointmagingProject.point.reservation.PointReservationRepository;
import joys.world.pointmagingProject.point.wellet.PointWalletRepository;
import org.junit.jupiter.api.AfterEach;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public abstract class BatchTestSupport {

    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private JobRepository jobRepository;

    @Autowired
    PointWalletRepository pointWalletRepository;
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    PointRepository pointRepository;
    @Autowired
    PointReservationRepository pointReservationRepository;

    protected JobExecution launchJob(Job job, JobParameters jobParameters) throws Exception {
        JobLauncherTestUtils jobLauncherTestUtils = new JobLauncherTestUtils();
        jobLauncherTestUtils.setJob(job);
        jobLauncherTestUtils.setJobLauncher(jobLauncher);
        jobLauncherTestUtils.setJobRepository(jobRepository);

        return jobLauncherTestUtils.launchJob(jobParameters == null ? new JobParametersBuilder().toJobParameters() : jobParameters);
    }

    @AfterEach
    protected void deleteAll() {
        pointReservationRepository.deleteAll();
        pointRepository.deleteAll();
        pointWalletRepository.deleteAll();
        messageRepository.deleteAll();
    }

}
