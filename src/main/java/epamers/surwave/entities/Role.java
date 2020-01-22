package epamers.surwave.entities;

import java.util.stream.Stream;
import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
  ADMIN,
  USER;

  public static String[] getAllowedAuthorities() {
    return Stream.of(Role.USER, Role.ADMIN)
        .map(Role::getAuthority)
        .toArray(String[]::new);
  }

  @Override
  public String getAuthority() {
    return name();
  }
}
