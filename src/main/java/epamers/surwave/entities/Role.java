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

//  public String getAuthorityTitle() {
//    return "ROLE_" + this.getAuthority();
//  }
  @Override
  public String getAuthority() {
    return name();
  }
}
