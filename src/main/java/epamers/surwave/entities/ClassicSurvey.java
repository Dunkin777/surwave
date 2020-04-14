package epamers.surwave.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.AllArgsConstructor;
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

  {
    setType(SurveyType.CLASSIC);
  }

  private Integer choicesByUser;

  public boolean isUserVoted(User user) {
    int currentVotes = getVotesByUserId(user.getId()).size();

    if (currentVotes == 0) {
      return false;
    } else if (currentVotes == choicesByUser) {
      return true;
    } else {
      throw new IllegalArgumentException("Found inconsistent number of votes for current user.");
    }
  }
}
