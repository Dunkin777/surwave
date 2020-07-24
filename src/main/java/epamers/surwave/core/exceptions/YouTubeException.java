package epamers.surwave.core.exceptions;

public class YouTubeException extends RuntimeException {

  private final String message;

  public YouTubeException(String message, Exception originalException) {
    super(originalException);
    this.message = message;
  }

  public YouTubeException(String message) {
    this.message = message;
  }

  @Override
  public String getMessage() {
    return message;
  }
}
