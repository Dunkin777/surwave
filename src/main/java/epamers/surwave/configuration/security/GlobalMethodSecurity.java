package epamers.surwave.configuration.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@Profile("!test")
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class GlobalMethodSecurity {

}
