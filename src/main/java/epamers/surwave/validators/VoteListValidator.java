package epamers.surwave.validators;

import static epamers.surwave.core.ExceptionMessageContract.VOTING_ALREADY_VOTED;
import static epamers.surwave.core.ExceptionMessageContract.VOTING_CHOICES_BY_USER_NOT_SATISFIED;
import static epamers.surwave.core.ExceptionMessageContract.VOTING_FOR_YOUR_OPTION;
import static epamers.surwave.core.ExceptionMessageContract.VOTING_FOR_ZERO_OPTIONS;
import static epamers.surwave.core.ExceptionMessageContract.VOTING_INVALID_RATING_FOR_CLASSIC_TYPE;
import static epamers.surwave.core.ExceptionMessageContract.VOTING_MORE_THAN_ONE_VOTE_FOR_OPTION;
import static epamers.surwave.core.ExceptionMessageContract.VOTING_NOT_ONE_SURVEY;
import static java.util.stream.Collectors.toSet;

import epamers.surwave.core.exceptions.VotingException;
import epamers.surwave.dtos.VoteForm;
import epamers.surwave.entities.ClassicSurvey;
import epamers.surwave.entities.Option;
import epamers.surwave.entities.Survey;
import epamers.surwave.entities.SurveyType;
import epamers.surwave.entities.User;
import epamers.surwave.services.SurveyService;
import epamers.surwave.services.UserService;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VoteListValidator implements Validator<List<VoteForm>> {

  private final UserService userService;
  private final SurveyService surveyService;

  @Override
  public void validate(List<VoteForm> votes) {
    if (votes.isEmpty()) {
      throw new VotingException(VOTING_FOR_ZERO_OPTIONS);
    }

    Set<Long> surveyIds = votes.stream()
        .map(VoteForm::getSurveyId)
        .collect(toSet());

    if (surveyIds.size() != 1) {
      throw new VotingException(VOTING_NOT_ONE_SURVEY);
    }

    Long surveyId = surveyIds.stream()
        .findFirst()
        .orElseThrow();
    Survey survey = surveyService.getById(surveyId);
    User participant = userService.getCurrent();

    if (survey.isUserVoted(participant)) {
      throw new VotingException(VOTING_ALREADY_VOTED);
    }

    checkNumberOfVotes(survey, votes);

    votes.forEach(v -> checkIndividualVote(v, participant, survey));
  }

  private void checkNumberOfVotes(Survey survey, List<VoteForm> votes) {
    int optionsChecked = votes.stream()
        .map(VoteForm::getOptionId)
        .collect(toSet()).size();

    if (votes.size() > optionsChecked) {
      throw new VotingException(VOTING_MORE_THAN_ONE_VOTE_FOR_OPTION);
    }

    if (survey.getType().equals(SurveyType.CLASSIC)) {
      Integer choicesByUser = ((ClassicSurvey) survey).getChoicesByUser();

      if (choicesByUser != votes.size()) {
        throw new VotingException(String.format(VOTING_CHOICES_BY_USER_NOT_SATISFIED, choicesByUser, optionsChecked));
      }
    }
  }

  private void checkIndividualVote(VoteForm vote, User participant, Survey survey) {
    Option option = surveyService.getOptionById(vote.getOptionId());

    if (option.getUser().equals(participant)) {
      throw new VotingException(VOTING_FOR_YOUR_OPTION);
    }

    if (survey.getType().equals(SurveyType.CLASSIC) && vote.getRating() != 1) {
      throw new VotingException(VOTING_INVALID_RATING_FOR_CLASSIC_TYPE);
    }
  }
}
