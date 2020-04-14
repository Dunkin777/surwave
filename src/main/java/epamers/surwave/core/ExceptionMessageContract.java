package epamers.surwave.core;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionMessageContract {

  public static final String VOTING_FOR_ZERO_OPTIONS = "Cannot vote for zero options.";
  public static final String VOTING_NOT_ONE_SURVEY = "Got incorrect number of surveys involved in voting.";
  public static final String VOTING_ALREADY_VOTED = "User already voted in this survey.";
  public static final String VOTING_MORE_THAN_ONE_VOTE_FOR_OPTION = "Cannot give more than one vote to the same option.";
  public static final String VOTING_CHOICES_BY_USER_NOT_SATISFIED = "Invalid number of votes! expected %d but received %d.";
  public static final String VOTING_FOR_YOUR_OPTION = "It is prohibited to vote for option that you've proposed.";
  public static final String VOTING_INVALID_RATING_FOR_CLASSIC_TYPE = "'1' is the only valid value for vote rating in classic surveys.";
}
