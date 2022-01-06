package joys.world.pointmagingProject.job.reservation;

import joys.world.pointmagingProject.job.validator.TodayJobParameterValidator;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExecutePointReservationJobConfig {

    @Bean
    public Job executePointReservationJob(
            JobBuilderFactory jobBuilderFactory ,
            TodayJobParameterValidator validator ,
            Step executePointReservationStep
    ) {
        return jobBuilderFactory.get("executePointReservationJob")
                .start(executePointReservationStep)
                .incrementer(new RunIdIncrementer())
                .validator(validator)
                .build();
    }

}
