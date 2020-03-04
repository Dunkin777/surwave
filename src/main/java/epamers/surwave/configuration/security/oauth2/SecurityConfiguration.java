package epamers.surwave.configuration.security.oauth2;

import epamers.surwave.configuration.security.AuthType;
import epamers.surwave.services.UserService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableOAuth2Sso
@Configuration
@ConditionalOnProperty(prefix = "surwave", name = "authType", havingValue = AuthType.OAUTH2)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers("swagger-ui.html", "/swagger**", "/webjars/**", "/v2/api-docs**").permitAll()
        .anyRequest().authenticated()
        .and()
        .csrf().disable();
  }

  @Bean
  public PrincipalExtractor principalExtractor(UserService userService) {
    return userService::getOrCreateFromGoogleData;
  }
}
