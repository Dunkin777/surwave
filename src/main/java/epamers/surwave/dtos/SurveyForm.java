package epamers.surwave.dtos;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import epamers.surwave.entities.SurveyState;
import epamers.surwave.entities.SurveyType;
import java.util.List;
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

  private Boolean isUsersSeparated;

  private Integer choicesByUser;

  @JsonSetter(nulls = Nulls.AS_EMPTY)
  private List<Long> optionIds;

  //From currently unused RangedSurvey. Don't forget to change/remove in the future
  private Boolean logarithmicRatingScale;
}
