package epamers.surwave.configuration.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthType {

  public static final String DISABLED = "disabled";
  public static final String BASIC = "basic";
  public static final String OAUTH2 = "oauth2";
}
