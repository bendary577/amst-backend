package accountmanager.supporttool.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;


@Configuration
@EnableAsync
@EnableScheduling
public class AsyncConfig {

    @Bean(name="taskExecutor")
    public Executor taskExecutor(){
        ThreadPoolTaskExecutor threadPoolExecutor = new ThreadPoolTaskExecutor();
        threadPoolExecutor.setCorePoolSize(3); //thread for each database (SIS - SSO) and another one for the scheduler
        threadPoolExecutor.setMaxPoolSize(3);
        threadPoolExecutor.setQueueCapacity(100);
        threadPoolExecutor.setThreadNamePrefix("appThread-");
        threadPoolExecutor.initialize();
        return threadPoolExecutor;
    }
}
