package epamers.surwave.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

//Added only to test storing of multiple entity types in one table. Unused for now.

@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("ranged")
@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class RangedSurvey extends Survey {

  @Builder.Default
  private final SurveyType type = SurveyType.RANGED;

  private Boolean logarithmicRatingScale;
}