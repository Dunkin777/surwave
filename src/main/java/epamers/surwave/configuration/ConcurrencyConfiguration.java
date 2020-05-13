package epamers.surwave.configuration;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class ConcurrencyConfiguration {

  @Bean
  public Executor threadPoolExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(4);
    executor.setMaxPoolSize(100);
    executor.setQueueCapacity(4);
    executor.setThreadNamePrefix("threadPoolExecutor-");
    executor.initialize();

    return executor;
  }
}
