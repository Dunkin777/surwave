package epamers.surwave.entities;

import java.io.Serializable;
import java.util.Arrays;

public enum Role implements Serializable {
  ADMIN,
  USER;

  public static String[] getAllowedAuthorities() {
    return Arrays.stream(values())
        .map(Role::getAuthorityTitle)
        .toArray(String[]::new);
  }

  public String getAuthorityTitle() {
    return "ROLE_" + name();
  }

  public static final String ROLE_ADMIN = "ROLE_ADMIN";
  public static final String ROLE_USER = "ROLE_USER";
}
