package epamers.surwave.configuration;

import epamers.surwave.entities.User;
import epamers.surwave.repos.UserRepository;
import java.time.LocalDateTime;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@EnableOAuth2Sso
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .mvcMatchers("/").permitAll()
        // .antMatchers("/swagger-ui.html", "/webjars/**", "/swagger-resources/**", "/v2/api-docs").permitAll()
        .anyRequest().authenticated()
        .and()
        .csrf().disable();
  }

  @Bean
  public PrincipalExtractor principalExtractor(UserRepository userRepository) {
    return googleData -> {
      String id = (String) googleData.get("sub");
      User user = userRepository.findById(id).orElseGet(() -> User.builder()
          .id(id)
          .name((String) googleData.get("name"))
          .email((String) googleData.get("email"))
          .locale((String) googleData.get("locale"))
          .userpic((String) googleData.get("picture"))
          .build()
      );
      user.setLastVisit(LocalDateTime.now());
      userRepository.save(user);
      return user;
    };
  }
}
