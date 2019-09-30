package epamers.surwave.dtos;

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

  private String description;

  private Integer proposalsByUser;

  private Boolean isUsersSeparated;

  private Integer choicesByUser;

  //From currently unused RangedSurvey. Don't forget to change/remove in the future
  private Boolean logarithmicRatingScale;
}
