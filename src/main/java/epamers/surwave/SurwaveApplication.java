package epamers.surwave;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SurwaveApplication {

  public static void main(String[] args) {
    SpringApplication.run(SurwaveApplication.class, args);
  }
}
