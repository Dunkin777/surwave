package epamers.surwave.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassicSurvey extends Survey {

  private final SurveyType type = SurveyType.CLASSIC;

  private Integer choicesByUser;
}
