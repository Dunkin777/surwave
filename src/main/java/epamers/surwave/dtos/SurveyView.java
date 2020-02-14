package epamers.surwave.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import epamers.surwave.entities.SurveyState;
import epamers.surwave.entities.SurveyType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Set;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@JsonInclude(Include.NON_NULL)
@ApiModel(description = "Resembles Survey with its settings, songs and current state.")
public class SurveyView {

  private Long id;

  @ApiModelProperty(notes = "Type of Survey.")
  private SurveyType type;

  @ApiModelProperty(notes = "Collection of Song objects, each can be voted for.")
  private Set<SongView> songs;

  @ApiModelProperty(notes = "Message left by Survey creator for every user who wants to vote.",
      example = "We need to pick some fast songs! And please, have mercy on drummer this time!")
  private String description;

  @ApiModelProperty(notes = "How many songs user allowed(should) to add to this survey.",
      example = "5")
  private Integer proposalsByUser;

  @ApiModelProperty(notes = "Stages of Survey's life.")
  private SurveyState state;

  @ApiModelProperty(notes = "How many songs user should pick in this survey when voting.",
      example = "1")
  private Integer choicesByUser;

  @ApiModelProperty(notes = "If equals 'true' then this survey should be hidden on the main page "
      + "from non-admins.")
  private Boolean isHidden;

  @ApiModelProperty(notes = "Setting only for RANGED Survey type. Currently unused.")
  private Boolean logarithmicRatingScale;
}
