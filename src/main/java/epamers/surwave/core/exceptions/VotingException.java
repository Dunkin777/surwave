package epamers.surwave.core.exceptions;

public class VotingException extends RuntimeException {

  private final String msg;

  public VotingException(String msg) {
    this.msg = msg;
  }

  public String getMsg() {
    return msg;
  }
}
