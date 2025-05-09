package config;

// 引入排程的 Job 類別
import job.WeatherJob;

// Quartz 核心排程元件
import org.quartz.*;

// Spring Framework 的註解設定
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // 標記為 Spring 設定類別，讓 Spring 容器啟動時載入
public class QuartzConfig {

    // 建立一個 JobDetail，代表要執行的排程工作。
    // 這裡指定 WeatherJob.class 作為執行任務的類別。
    @Bean
    public JobDetail jobDetail() {
        return JobBuilder.newJob(WeatherJob.class) // 指定要執行的 Job 類別
                .withIdentity("weatherJob")        // 為這個 Job 命名（唯一識別）
                .storeDurably()                    // 即使沒有 Trigger 也要保留這個 Job
                .build();                          // 建構 JobDetail 實例
    }

    
    // 建立一個觸發器（Trigger），用來定義什麼時間執行 Job。
    // 這裡使用 Cron 表達式排定每天 11:00 執行。
    @Bean
    public Trigger trigger() {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail()) // 指定這個觸發器對應的 Job
                .withIdentity("weatherTrigger") // 為觸發器命名
                .withSchedule(
                    CronScheduleBuilder.cronSchedule("0 0 14 * * ?")
                    // 每天 11:00 AM 執行（Quartz Cron 格式）
                    // 說明：秒 分 時 日 月 星期（? 表示不指定）
                )
                .build(); // 建構 Trigger 實例
    }
}
