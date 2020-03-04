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
@ApiModel(description = "Resembles Survey with its properties.")
public class SurveyForm {

  @NotNull
  @ApiModelProperty(required = true, notes = "Type of Survey. RANGED is currently unused.")
  private SurveyType type;

  @ApiModelProperty(notes = "State. Use CREATED for new Surveys, change to STARTED to finish "
      + "collecting Songs and open survey for actual voting.")
  private SurveyState state;

  @ApiModelProperty(notes = "Title of whole survey, usually its purpose.",
      example = "Songs for New Year party")
  private String title;

  @ApiModelProperty(notes = "Message left by Survey creator for every user who wants to vote.",
      example = "We need to pick some fast songs! And please, have mercy on drummer this time!")
  private String description;

  @NotNull
  @ApiModelProperty(required = true, notes = "Defines how many songs one user should propose.",
      example = "5")
  private Integer proposalsByUser;

  @ApiModelProperty(notes = "How many songs user should pick when voting. Only for CLASSIC surveys.",
      example = "3")
  private Integer choicesByUser;

  @NotNull
  @ApiModelProperty(notes = "If 'true' then survey should not be visible for non-admins.",
      example = "true")
  private Boolean isHidden;

  @ApiModelProperty(notes = "Needed only for RANGED Survey type. Currently unused.",
      example = "false")
  private Boolean logarithmicRatingScale;
}
