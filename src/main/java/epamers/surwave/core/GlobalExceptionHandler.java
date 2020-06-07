package epamers.surwave.core;

import static epamers.surwave.core.ExceptionMessageContract.REQUEST_SIZE_IS_TOO_BIG;
import static epamers.surwave.core.ExceptionMessageContract.SONG_FILE_IS_TOO_BIG;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import epamers.surwave.core.exceptions.FileStorageException;
import epamers.surwave.core.exceptions.NotAuthenticatedException;
import epamers.surwave.core.exceptions.ResultsException;
import epamers.surwave.core.exceptions.VotingException;
import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadBase.SizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
      EntityNotFoundException.class, ConstraintViolationException.class, ResultsException.class, HttpMessageNotReadableException.class,
      InvalidFormatException.class})
  public String handleIllegalArgumentException(RuntimeException ex) {
    log.debug(EXCEPTION_MESSAGE, 400, ex);

    return ex.getMessage();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({MethodArgumentNotValidException.class})
  public String handleJavaxValidationException(MethodArgumentNotValidException ex) {
    log.debug(EXCEPTION_MESSAGE, 400, ex);

    StringBuilder message = new StringBuilder();
    ex.getBindingResult().getAllErrors().forEach(e -> message.append(buildFieldError(e)));

    return message.toString().trim();
  }

  private String buildFieldError(ObjectError objectError) {
    String errorMessage;

    if (objectError instanceof FieldError) {
      FieldError error = (FieldError) objectError;
      errorMessage = StringUtils.capitalize(error.getField()) + ": " + error.getDefaultMessage();
    } else {
      errorMessage = objectError.getObjectName() + ": " + objectError.getDefaultMessage();
    }

    return errorMessage + ". ";
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

  @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
  @ExceptionHandler(SizeLimitExceededException.class)
  public String handleRequestException(SizeLimitExceededException ex) {
    log.debug(EXCEPTION_MESSAGE, 413, ex);

    return REQUEST_SIZE_IS_TOO_BIG;
  }
}
