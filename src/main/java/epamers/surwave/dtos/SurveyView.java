package epamers.surwave.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import epamers.surwave.entities.SurveyState;
import epamers.surwave.entities.SurveyType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@JsonInclude(Include.NON_NULL)
@ApiModel(description = "Resembles Survey with its properties, songs and current state.")
public class SurveyView {

  private Long id;

  @ApiModelProperty(notes = "Type of Survey.")
  private SurveyType type;

  @ApiModelProperty(notes = "Stage of Survey's lifecycle.")
  private SurveyState state;

  @ApiModelProperty(notes = "Title of whole survey, usually its purpose.")
  private String title;

  @ApiModelProperty(notes = "Message left by Survey creator for every user who wants to vote.")
  private String description;

  @ApiModelProperty(notes = "Collection of Options, each representing one Song")
  private List<OptionView> options;

  @ApiModelProperty(notes = "How many songs one user should add to this survey.")
  private Integer proposalsByUser;

  @ApiModelProperty(notes = "How many songs user should pick when voting. Only for CLASSIC surveys.")
  private Integer choicesByUser;

  @ApiModelProperty(notes = "If 'true' then survey should not be visible for non-admins.")
  private Boolean isHidden;

  @ApiModelProperty(notes = "Setting only for RANGED Survey type. Currently unused.")
  private Boolean logarithmicRatingScale;

  @ApiModelProperty(notes = "If 'true' then current user already voted in this survey.")
  private Boolean isVoted;
}
