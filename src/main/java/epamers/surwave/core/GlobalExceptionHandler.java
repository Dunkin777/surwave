package epamers.surwave.core;

import epamers.surwave.core.exceptions.FileStorageException;
import epamers.surwave.core.exceptions.NotAuthenticatedException;
import epamers.surwave.core.exceptions.VotingException;
import javax.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  private static final String EXCEPTION_MESSAGE = "Responded with {} code because of exception: ";

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class, VotingException.class, FileStorageException.class,
      EntityNotFoundException.class, MethodArgumentNotValidException.class})
  public String handleIllegalArgumentException(RuntimeException ex) {
    log.debug(EXCEPTION_MESSAGE, 400, ex);

    return ex.getMessage();
  }

  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler(NotAuthenticatedException.class)
  public String handleNotAuthenticatedException(RuntimeException ex) {
    log.debug(EXCEPTION_MESSAGE, 401, ex);

    return ex.getMessage();
  }
}
