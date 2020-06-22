package epamers.surwave.core.exceptions;

import lombok.Getter;

@Getter
public class FileStorageException extends RuntimeException {

  private final String message;

  public FileStorageException(String message, Exception originalException) {
    super(originalException);
    this.message = message;
  }

  public FileStorageException(String message) {
    this.message = message;
  }

  @Override
  public String getMessage() {
    return message;
  }
}
