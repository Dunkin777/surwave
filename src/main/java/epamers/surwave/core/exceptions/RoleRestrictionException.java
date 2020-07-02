package epamers.surwave.core.exceptions;

public class RoleRestrictionException extends RuntimeException {

  private final String message;

  public RoleRestrictionException(String message) {
    this.message = message;
  }

  @Override
  public String getMessage() {
    return message;
  }
}
