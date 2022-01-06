package joys.world.pointmagingProject.job.expire;

import joys.world.pointmagingProject.point.point.Point;
import joys.world.pointmagingProject.point.point.PointRepository;
import joys.world.pointmagingProject.point.wellet.PointWallet;
import joys.world.pointmagingProject.point.wellet.PointWalletRepository;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.util.Map;

@Configuration
public class ExpirePointStepConfig {
    @Bean
    @JobScope
    public Step expirePointStep(
            StepBuilderFactory stepBuilderFactory,
            PlatformTransactionManager platformTransactionManager,
            JpaPagingItemReader<Point> expirePointItemReader,
            ItemProcessor<Point, Point> expirePointItemProcessor,
            ItemWriter<Point> expirePointItemWriter

    ) {
        return stepBuilderFactory
                .get("expirePointStep")
                .allowStartIfComplete(true)
                .transactionManager(platformTransactionManager)
                .<Point, Point>chunk(1000)
                .reader(expirePointItemReader)
                .processor(expirePointItemProcessor)
                .writer(expirePointItemWriter)
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<Point> expirePointItemReader(
            EntityManagerFactory entityManagerFactory,
            @Value("#{T(java.time.LocalDate).parse(jobParameters[today])}")
                    LocalDate today
    ) {
        return new JpaPagingItemReaderBuilder<Point>()
                .name("expirePointItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select p from Point p where p.expireDate < :today and is_used = false and is_expired = false")
                .parameterValues(Map.of("today", today))
                .pageSize(1000)
                .build();
    }


    @Bean
    @StepScope
    public ItemProcessor<Point, Point> expirePointItemProcessor() {
        return point -> {
            point.setExpired(true);
            PointWallet wallet = point.getPointWallet();
            wallet.setAmount(wallet.getAmount() - (point.getAmount()));
            return point;
        };
    }

    @Bean
    @StepScope
    public ItemWriter<Point> expirePointItemWriter(
            PointRepository pointRepository,
            PointWalletRepository pointWalletRepository
    ) {
        return points -> {
            points.forEach(point -> {
                if (point.isExpired()) {
                    pointRepository.save(point);
                    pointWalletRepository.save(point.getPointWallet());
                }
            });
        };
    }

}
