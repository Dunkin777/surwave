package epamers.surwave.core;

import static epamers.surwave.core.ExceptionMessageContract.SONG_FILE_IS_TOO_BIG;

import epamers.surwave.core.exceptions.FileStorageException;
import epamers.surwave.core.exceptions.NotAuthenticatedException;
import epamers.surwave.core.exceptions.ResultsException;
import epamers.surwave.core.exceptions.VotingException;
import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  private static final String EXCEPTION_MESSAGE = "Responded with {} code because of exception: ";

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class, VotingException.class, FileStorageException.class,
      EntityNotFoundException.class, ConstraintViolationException.class, ResultsException.class})
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

  @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public String handleMultipartException(MaxUploadSizeExceededException ex) {
    log.debug(EXCEPTION_MESSAGE, 413, ex);

    return SONG_FILE_IS_TOO_BIG;
  }
}
