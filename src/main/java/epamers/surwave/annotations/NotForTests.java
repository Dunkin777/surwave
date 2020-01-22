package epamers.surwave.annotations;

import org.springframework.context.annotation.Profile;

@Profile("!test")
public @interface NotForTests {

}
