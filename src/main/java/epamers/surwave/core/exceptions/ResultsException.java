package epamers.surwave.core.exceptions;

public class ResultsException extends RuntimeException {

  private final String msg;

  public ResultsException(String msg) {
    this.msg = msg;
  }

  @Override
  public String getMessage() {
    return msg;
  }
}
