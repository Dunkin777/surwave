package epamers.surwave.services;

import epamers.surwave.entities.Option;
import epamers.surwave.entities.Song;
import epamers.surwave.entities.Survey;
import epamers.surwave.entities.SurveyState;
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

  public Survey getByIdForCurrentUser(Long id, User user) {
    Survey survey = getById(id);
    User currentUser = userService.getById(user.getId());

    Set<Option> options = survey.getOptions().stream()
        .filter(option -> !option.getUser().equals(currentUser))
        .collect(Collectors.toSet());

    survey.setOptions(options);

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
  public void addVotes(List<Vote> votes) {
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
}
