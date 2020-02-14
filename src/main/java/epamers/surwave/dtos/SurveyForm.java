package epamers.surwave.dtos;

import epamers.surwave.entities.SurveyState;
import epamers.surwave.entities.SurveyType;
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
@ApiModel(description = "Resembles Survey with all its settings.")
public class SurveyForm {

  @ApiModelProperty(required = true, notes = "Type of Survey, obviously. RANGED is currently "
      + "unused.", example = "CLASSIC")
  @NotNull
  private SurveyType type;

  @ApiModelProperty(notes = "State. You can skip that if you're just creating Survey. "
      + "Choose STARTED state to finish collecting Songs and open survey for actual voting.",
      example = "STARTED")
  private SurveyState state;

  @ApiModelProperty(notes = "Some notes on this survey's goals and what should "
      + "participants keep in mind while voting.", example = "We need to pick some fast songs! And "
      + "please, have mercy on drummer this time!")
  private String description;

  @NotNull
  @ApiModelProperty(required = true, notes = "Defines how many songs one user should propose.",
      example = "5")
  private Integer proposalsByUser;

  @ApiModelProperty(notes = "Needed only for CLASSIC Survey Type. Defines how many song should one "
      + "user pick.", example = "3")
  private Integer choicesByUser;

  @ApiModelProperty(required = true, notes = "Should we hide that Survey from main page list for "
      + "normal users? Can be used for example for Surveys that we want to pause for some time or "
      + "just to clear some space on main page.", example = "true")
  @NotNull
  private Boolean isHidden;

  @ApiModelProperty(notes = "Needed only for RANGED Survey type. Currently unused.", example = "false")
  private Boolean logarithmicRatingScale;
}
