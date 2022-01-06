package joys.world.pointmagingProject.job.message;

import joys.world.pointmagingProject.job.validator.TodayJobParameterValidator;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageExpiredPointJobConfiguration {

    @Bean
    public Job messageExpiredJob(
            JobBuilderFactory jobBuilderFactory,
            TodayJobParameterValidator validator,
            Step messageExpiredStep
    ) {
        return jobBuilderFactory
                .get("messageExpiredPointJob")
                .validator(validator)
                .incrementer(new RunIdIncrementer())
                .start(messageExpiredStep)
                .build();
    }

}
