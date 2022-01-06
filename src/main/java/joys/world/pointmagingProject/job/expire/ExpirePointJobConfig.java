package joys.world.pointmagingProject.job.expire;

import joys.world.pointmagingProject.job.validator.TodayJobParameterValidator;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExpirePointJobConfig {

    @Bean
    public Job expirePointJob(
            JobBuilderFactory jobBuilderFactory ,
            Step expirePointStep ,
            TodayJobParameterValidator todayJobParameterValidator
    ) {
        return jobBuilderFactory.get("expirePointJob")
                .start(expirePointStep)
                .incrementer(new RunIdIncrementer())
                .validator(todayJobParameterValidator)
                .build();
    }

}
