package epamers.surwave.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("classic")
@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ClassicSurvey extends Survey {

  @Default
  private final SurveyType type = SurveyType.CLASSIC;

  private Integer choicesByUser;
}
