package epamers.surwave.core;

import static epamers.surwave.core.ExceptionMessageContract.REQUEST_SIZE_IS_TOO_BIG;
import static epamers.surwave.core.ExceptionMessageContract.SONG_FILE_IS_TOO_BIG;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import epamers.surwave.core.exceptions.FileStorageException;
import epamers.surwave.core.exceptions.NotAuthenticatedException;
import epamers.surwave.core.exceptions.ValidationException;
import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadBase.SizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
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

  private static final String EXCEPTION_MESSAGE = "Responded with {} code. Cause: {}";

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Exception.class)
  public ExceptionMessage handleUnexpectedException(Exception ex) {
    String message = "Unexpected internal error occurred on the server";

    return buildMessage(ex, 500, message);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class, ValidationException.class, FileStorageException.class,
      EntityNotFoundException.class, ConstraintViolationException.class, HttpMessageNotReadableException.class, InvalidFormatException.class})
  public ExceptionMessage handleBadRequestException(RuntimeException ex) {
    return buildMessage(ex, 400);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ExceptionMessage handleJavaxValidationException(MethodArgumentNotValidException ex) {
    StringBuilder messageBuilder = new StringBuilder();
    ex.getBindingResult().getAllErrors().forEach(e -> messageBuilder.append(buildFieldError(e)));
    String message = messageBuilder.toString().trim();

    return buildMessage(ex, 400, message);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(BindException.class)
  public ExceptionMessage handleJavaxBindException(BindException ex) {
    StringBuilder messageBuilder = new StringBuilder();
    ex.getBindingResult().getAllErrors().forEach(e -> messageBuilder.append(buildFieldError(e)));
    String message = messageBuilder.toString().trim();

    return buildMessage(ex, 400, message);
  }

  private String buildFieldError(ObjectError objectError) {
    String errorMessage;

    if (objectError instanceof FieldError) {
      FieldError error = (FieldError) objectError;
      errorMessage = StringUtils.capitalize(error.getField()) + ": " + error.getDefaultMessage();
    } else {
      errorMessage = objectError.getObjectName() + ": " + objectError.getDefaultMessage();
    }

    return errorMessage;
  }

  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler(NotAuthenticatedException.class)
  public ExceptionMessage handleNotAuthenticatedException(RuntimeException ex) {
    return buildMessage(ex, 401);
  }

  @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ExceptionMessage handleMultipartException(MaxUploadSizeExceededException ex) {
    return buildMessage(ex, 413, SONG_FILE_IS_TOO_BIG);
  }

  @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
  @ExceptionHandler(SizeLimitExceededException.class)
  public ExceptionMessage handleRequestException(SizeLimitExceededException ex) {
    return buildMessage(ex, 413, REQUEST_SIZE_IS_TOO_BIG);
  }

  private ExceptionMessage buildMessage(Exception ex, int code) {
    log.info(EXCEPTION_MESSAGE, code, ex.getMessage());
    log.debug("Stacktrace: ", ex);

    return new ExceptionMessage(ex.getMessage());
  }

  private ExceptionMessage buildMessage(Exception ex, int code, String message) {
    log.info(EXCEPTION_MESSAGE, code, message);
    log.debug("Stacktrace: ", ex);

    return new ExceptionMessage(message);
  }
}
