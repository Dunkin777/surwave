package epamers.surwave.core.exceptions;

public class NotAuthenticatedException extends RuntimeException {

  private final String msg;

  public NotAuthenticatedException(String msg) {
    this.msg = msg;
  }

  @Override
  public String getMessage() {
    return msg;
  }
}
