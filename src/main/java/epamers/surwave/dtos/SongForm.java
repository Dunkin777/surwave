package epamers.surwave.dtos;

import io.swagger.annotations.ApiModel;
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
@ApiModel(description = "Resembles one Song that can be used as option in some survey.")
public class SongForm {

  @NotNull
  @ApiModelProperty(required = true, notes = "Band name, if you wish.", example = "John Lennon")
  private String performer;

  @NotNull
  @ApiModelProperty(required = true, notes = "Name of the composition.", example = "Fly As A Bird")
  private String title;

  @ApiModelProperty(notes = "e.g. How to play, what you planned to change from original version...",
      example = "I love this song! Let's play it 2 times faster!")
  private String comment;

  @NotNull
  @ApiModelProperty(required = true, notes = "Survey id that will contain that song.", example = "15")
  private Long surveyId;
}
