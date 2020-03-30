package epamers.surwave.services;

import epamers.surwave.core.exceptions.VotingException;
import epamers.surwave.entities.ClassicSurvey;
import epamers.surwave.entities.Option;
import epamers.surwave.entities.Song;
import epamers.surwave.entities.Survey;
import epamers.surwave.entities.SurveyState;
import epamers.surwave.entities.SurveyType;
import epamers.surwave.entities.User;
import epamers.surwave.entities.Vote;
import epamers.surwave.repos.OptionRepository;
import epamers.surwave.repos.SurveyRepository;
import epamers.surwave.repos.VoteRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SurveyService {

  private final SurveyRepository surveyRepository;
  private final SongService songService;
  private final UserService userService;
  private final OptionRepository optionRepository;
  private final VoteRepository voteRepository;

  public List<Survey> getAll() {
    return surveyRepository.findAll();
  }

  public Survey getById(Long id) {
    return surveyRepository.findById(id).orElseThrow();
  }

  public Option getOptionById(Long id) {
    return optionRepository.findById(id).orElseThrow();
  }

  public Survey getByIdFiltered(Long id, User user) {
    Survey survey = getById(id);

    return filterOptions(survey, user);
  }

  public List<Survey> getAllFiltered(User user) {
    List<Survey> surveys = getAll().stream()
        .filter(s -> !s.getIsHidden() || user.isAdmin())
        .collect(Collectors.toList());
    surveys.forEach(survey -> filterOptions(survey, user));

    return surveys;
  }

  private Survey filterOptions(Survey survey, User user) {
    Set<Option> options = survey.getOptions();
    Set<Option> filteredOptions;

    if (survey.getState() == SurveyState.CREATED) {
      filteredOptions = options.stream()
          .filter(option -> option.getUser().equals(user))
          .collect(Collectors.toSet());

    } else if (survey.getState() == SurveyState.STARTED) {
      filteredOptions = options.stream()
          .filter(option -> !option.getUser().equals(user))
          .collect(Collectors.toSet());

    } else {
      filteredOptions = options;
    }

    survey.setOptions(filteredOptions);

    return survey;
  }

  @Transactional
  public Survey create(Survey survey) {
    if (survey == null) {
      throw new IllegalArgumentException();
    }

    survey.setState(SurveyState.CREATED);

    return surveyRepository.save(survey);
  }

  @Transactional
  public void update(Long id, Survey survey) {
    if (survey == null) {
      throw new IllegalArgumentException();
    }

    Survey storedSurvey = getById(id);
    storedSurvey.setDescription(survey.getDescription());
    storedSurvey.setTitle(survey.getTitle());
    storedSurvey.setState(survey.getState());
    storedSurvey.setIsHidden(survey.getIsHidden());
  }

  @Transactional
  public void removeOption(Long optionId) {
    Option optionToRemove = getOptionById(optionId);
    optionRepository.delete(optionToRemove);
  }

  @Transactional
  public Option addOption(Long surveyId, Option option, User currentUser) {
    if (option == null) {
      throw new IllegalArgumentException();
    }

    Survey survey = getById(surveyId);
    Song song = songService.getById(option.getSong().getId());

    if (survey.getSongs().contains(song)) {
      throw new IllegalArgumentException("Given survey already contains this song.");
    }

    option.setSurvey(survey);
    option.setUser(currentUser);
    option.setSong(song);

    return optionRepository.save(option);
  }

  @Transactional
  public void addVotes(Long surveyId, List<Vote> votes) {
    Survey survey = getById(surveyId);

    if (survey.getType().equals(SurveyType.CLASSIC)) {
      checkVotesSize((ClassicSurvey) survey, votes);
    }

    for (Vote vote : votes) {
      Long optionId = vote.getOption().getId();
      Option option = optionRepository.getOne(optionId);

      String participantId = vote.getParticipant().getId();
      User participant = userService.getById(participantId);

      vote.setOption(option);
      vote.setParticipant(participant);
      voteRepository.save(vote);
    }
  }

  private void checkVotesSize(ClassicSurvey survey, List<Vote> votes) {
    Integer choicesByUser = survey.getChoicesByUser();
    int votesSize = votes.size();

    if (choicesByUser != votesSize) {
      throw new VotingException(String.format("Invalid number of votes! expected %d but received %d", choicesByUser, votesSize));
    }
  }
}
