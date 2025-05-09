package Quartz_demo2;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"service","config", "job", "Quartz_demo2"})
@MapperScan("repository")
public class QuartzDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(QuartzDemoApplication.class, args);
    }
}
