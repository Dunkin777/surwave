package epamers.surwave.dtos;

import epamers.surwave.entities.SurveyState;
import epamers.surwave.entities.SurveyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurveyForm {

  private SurveyType type;

  private SurveyState state;

  private String description;

  private Integer proposalsByUser;

  private Integer choicesByUser;

  //From currently unused RangedSurvey. Don't forget to change/remove in the future
  private Boolean logarithmicRatingScale;
}
