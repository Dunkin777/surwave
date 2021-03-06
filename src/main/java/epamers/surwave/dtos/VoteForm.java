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
@ApiModel(description = "Resembles Vote action by specific User.")
public class VoteForm {

  @NotNull
  @ApiModelProperty(required = true, notes = "Id of Option that user voted for.", example = "42")
  private Long optionId;

  @NotNull
  @ApiModelProperty(required = true, notes = "Resembles the fact of vote itself, preferably 0 or 1, also represents"
      + " position of Option in RANGED survey, currently unused.", example = "1")
  private Integer rating;

  @NotNull
  @ApiModelProperty(required = true, notes = "Survey in which voting occurs.", example = "3")
  private Long surveyId;
}
