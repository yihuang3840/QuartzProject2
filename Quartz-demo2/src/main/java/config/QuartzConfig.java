package config;

import job.WeatherJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail weatherJobDetail() {
        return JobBuilder.newJob(WeatherJob.class)
                .withIdentity("weatherJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger weatherJobTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(weatherJobDetail())
                .withIdentity("weatherTrigger")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                    .withIntervalInSeconds(10)
                    .repeatForever())
                .build();
    }
}
