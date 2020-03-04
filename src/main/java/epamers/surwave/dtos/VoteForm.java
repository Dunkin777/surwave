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
  @ApiModelProperty(required = true, notes = "Id of Option that user voted for.")
  private Long optionId;

  @NotNull
  @ApiModelProperty(required = true, notes = "Id of voted User")
  private String participantId;

  @NotNull
  @ApiModelProperty(required = true, notes = "Resembles the fact of vote itself, preferably 0 or 1, also represents"
      + " position of Option in RANGED survey, currently unused")
  private Integer rating;
}
