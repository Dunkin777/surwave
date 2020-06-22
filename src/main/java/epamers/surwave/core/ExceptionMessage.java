package epamers.surwave.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
@AllArgsConstructor
public class ExceptionMessage {

  private final String message;
}
