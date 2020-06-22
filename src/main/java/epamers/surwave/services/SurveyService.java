package epamers.surwave.services;

import static epamers.surwave.core.ExceptionMessageContract.OPTION_ALREADY_EXISTS;
import static epamers.surwave.core.ExceptionMessageContract.OPTION_IS_NULL_CREATION;
import static epamers.surwave.core.ExceptionMessageContract.OPTION_NOT_FOUND;
import static epamers.surwave.core.ExceptionMessageContract.RESULTS_INVALID_SURVEY_STATE;
import static epamers.surwave.core.ExceptionMessageContract.SURVEY_IS_NULL_CREATION;
import static epamers.surwave.core.ExceptionMessageContract.SURVEY_IS_NULL_MODIFICATION;
import static epamers.surwave.core.ExceptionMessageContract.SURVEY_NOT_FOUND;
import static java.util.stream.Collectors.toSet;

import epamers.surwave.core.exceptions.ValidationException;
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
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SurveyService {

  private final SurveyRepository surveyRepository;
  private final SongService songService;
  private final OptionRepository optionRepository;
  private final VoteRepository voteRepository;

  public List<Survey> getAll() {
    return surveyRepository.findAll();
  }

  public Survey getById(Long id) {
    return surveyRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(String.format(SURVEY_NOT_FOUND, id)));
  }

  public Survey getByIdWithSongURLs(Long id) {
    Survey survey = getById(id);
    survey.getSongs().forEach(songService::fillWithMediaUrl);

    return survey;
  }

  public Survey getByIdForRating(Long id) {
    Survey survey = getByIdWithSongURLs(id);

    if (survey.getState() != SurveyState.STOPPED) {
      throw new ValidationException(RESULTS_INVALID_SURVEY_STATE);
    }

    return survey;
  }

  public Option getOptionById(Long id) {
    return optionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format(OPTION_NOT_FOUND, id)));
  }

  public Survey getByIdFiltered(Long id, User user) {
    Survey survey = getByIdWithSongURLs(id);

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
          .collect(toSet());
    } else if (survey.getState() == SurveyState.STARTED) {
      filteredOptions = options.stream()
          .filter(option -> !option.getUser().equals(user))
          .collect(toSet());
    } else {
      filteredOptions = options;
    }

    survey.setOptions(filteredOptions);

    return survey;
  }

  @Transactional
  public Survey create(Survey survey) {
    if (survey == null) {
      throw new IllegalArgumentException(SURVEY_IS_NULL_CREATION);
    }

    survey.setState(SurveyState.CREATED);

    return surveyRepository.save(survey);
  }

  @Transactional
  public void update(Long id, Survey survey) {
    if (survey == null) {
      throw new IllegalArgumentException(SURVEY_IS_NULL_MODIFICATION);
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
      throw new IllegalArgumentException(OPTION_IS_NULL_CREATION);
    }

    Survey survey = getById(surveyId);
    Song song = songService.getById(option.getSong().getId());

    if (survey.getSongs().contains(song)) {
      throw new IllegalArgumentException(OPTION_ALREADY_EXISTS);
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
      Option option = getOptionById(optionId);

      vote.setOption(option);
      voteRepository.save(vote);
    }
  }
}
