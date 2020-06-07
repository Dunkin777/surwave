package epamers.surwave.core.exceptions;

import lombok.Getter;

@Getter
public class FileStorageException extends RuntimeException {

  private final String msg;

  public FileStorageException(String msg, Exception originalException) {
    super(originalException);
    this.msg = msg;
  }

  public FileStorageException(String msg) {
    this.msg = msg;
  }

  @Override
  public String getMessage() {
    return msg;
  }
}
