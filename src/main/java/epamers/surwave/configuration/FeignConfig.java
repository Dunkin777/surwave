package epamers.surwave.configuration;

import epamers.surwave.clients.AnalyticsClient;
import feign.Feign;
import feign.Logger;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

  @Bean
  AnalyticsClient analyticsClient(@Value("${surwave.analyticsUrl}") String analyticsUrl) {
    return Feign.builder()
        .contract(new SpringMvcContract())
        .encoder(new JacksonEncoder())
        .decoder(new JacksonDecoder())
        .logger(new Logger.ErrorLogger())
        .logLevel(Logger.Level.BASIC)
        .target(AnalyticsClient.class, analyticsUrl);
  }
}
