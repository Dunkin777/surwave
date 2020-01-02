package epamers.surwave.dtos;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongForm {

  @NotNull
  @ApiModelProperty(required = true)
  private String performer;

  @NotNull
  @ApiModelProperty(required = true)
  private String title;

  private String comment;

  @NotNull
  @ApiModelProperty(required = true)
  private Long surveyId;
}
