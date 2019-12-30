package epamers.surwave.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import epamers.surwave.entities.SurveyState;
import epamers.surwave.entities.SurveyType;
import java.util.Set;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@JsonInclude(Include.NON_NULL)
public class SurveyView {

  private Long id;

  private SurveyType type;

  private Set<SongView> songs;

  private String description;

  private Integer proposalsByUser;

  private SurveyState state;

  private Integer choicesByUser;

  private Boolean isHidden;

  //From currently unused RangedSurvey. Don't forget to change/remove in the future
  private Boolean logarithmicRatingScale;
}
