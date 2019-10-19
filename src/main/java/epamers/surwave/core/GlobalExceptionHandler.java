package epamers.surwave.core;

import java.util.NoSuchElementException;
import javax.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(EntityNotFoundException.class)
  public String handleEntityNotFoundException(EntityNotFoundException ex) {

    log.debug("EntityNotFoundException: ", ex);
    return ex.getMessage();
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(NoSuchElementException.class)
  public String handleNoSuchElementException(NoSuchElementException ex) {

    log.debug("NoSuchElementException: ", ex);
    return ex.getMessage();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(IllegalArgumentException.class)
  public String handleIllegalArgumentException(IllegalArgumentException ex) {

    log.debug("IllegalArgumentException: ", ex);
    return ex.getMessage();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(IllegalStateException.class)
  public String handleIllegalStateException(IllegalArgumentException ex) {

    log.debug("IllegalStateException: ", ex);
    return ex.getMessage();
  }
}
