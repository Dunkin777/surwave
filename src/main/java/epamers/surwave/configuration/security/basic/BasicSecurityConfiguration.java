package epamers.surwave.configuration.security.basic;

import epamers.surwave.configuration.security.AuthType;
import epamers.surwave.entities.Role;
import epamers.surwave.entities.User;
import epamers.surwave.repos.UserRepository;
import java.util.HashMap;
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
@EnableWebSecurity
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@ConditionalOnProperty(prefix = "surwave", name = "authType", havingValue = AuthType.BASIC)
public class BasicSecurityConfiguration extends WebSecurityConfigurerAdapter {

  private final BasicAuthenticationEntryPoint authEntryPoint;
  private final UserRepository userRepository;

  public BasicSecurityConfiguration(BasicAuthenticationEntryPoint authEntryPoint, UserRepository userRepository) {
    this.authEntryPoint = authEntryPoint;
    this.userRepository = userRepository;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        .antMatcher("/**")
        .authorizeRequests()
        .antMatchers("/**")
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

    User user = new User(new HashMap<>() {{
      put("sub", "coolID");
      put("name", "guest");
      put("email", "GuestEmail@mail.dut");
      put("locale", "native");
      put("picture", "nopicture");
    }});

    user.setPassword("$2a$10$of1z7we8YS4xth0a.S71WOzIOqJ7ms/XVASVCjtl8el1psQLLq.0K");
    detailsManager.createUser(user);
    return detailsManager;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
