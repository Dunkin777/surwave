package epamers.surwave.configuration.security.basic;

import epamers.surwave.configuration.security.AuthType;
import epamers.surwave.entities.Role;
import epamers.surwave.entities.User;
import epamers.surwave.repos.UserRepository;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@ConditionalOnProperty(prefix = "surwave", name = "authType", havingValue = AuthType.BASIC)
public class BasicSecurityConfiguration extends WebSecurityConfigurerAdapter {

  private final BasicAuthenticationEntryPoint authEntryPoint;
  private final UserRepository userRepository;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable()
        .authorizeRequests()
        .anyRequest()
        .hasAnyAuthority(Role.getAllowedAuthorities())
        .and()
        .httpBasic().authenticationEntryPoint(authEntryPoint)
        .and()
        .logout().logoutUrl("/logout").clearAuthentication(true).invalidateHttpSession(true);

    http.headers().frameOptions().disable();
  }

  @Autowired
  public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(basicUserDetailsManager())
        .passwordEncoder(passwordEncoder());
  }

  @Bean
  public UserDetailsManager basicUserDetailsManager() {
    BasicUserDetailsManager detailsManager = new BasicUserDetailsManager(userRepository);

    User user = new User(Map.of(
        "sub", "coolID",
        "name", "guest",
        "email", "GuestEmail@mail.dut",
        "locale", "ru",
        "picture", "nopicture"));

    User anotherUser = new User(Map.of(
        "sub", "coolID2",
        "name", "admin",
        "email", "GuestEmail2@mail.dut",
        "locale", "en-GB",
        "picture", "nopicture"));

    user.setPassword("$2a$10$of1z7we8YS4xth0a.S71WOzIOqJ7ms/XVASVCjtl8el1psQLLq.0K");
    detailsManager.updateUser(user);

    anotherUser.setPassword("$2a$10$of1z7we8YS4xth0a.S71WOzIOqJ7ms/XVASVCjtl8el1psQLLq.0K");
    anotherUser.addRole(Role.ADMIN);
    detailsManager.updateUser(anotherUser);

    return detailsManager;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
