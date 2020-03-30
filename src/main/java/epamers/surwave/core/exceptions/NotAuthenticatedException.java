package epamers.surwave.core.exceptions;

public class NotAuthenticatedException extends RuntimeException {

  private final String msg;

  public NotAuthenticatedException(String msg) {
    this.msg = msg;
  }

  public String getMsg() {
    return msg;
  }
}
