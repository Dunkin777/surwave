package epamers.surwave.entities;

import java.util.Arrays;
import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
  ADMIN,
  USER;

  public static String[] getAllowedAuthorities() {
    return Arrays.stream(values())
        .map(Role::getAuthority)
        .toArray(String[]::new);
  }

  @Override
  public String getAuthority() {
    return name();
  }
}
