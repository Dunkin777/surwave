package epamers.surwave.services;

import static java.util.stream.Collectors.toSet;

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
  private final MediaFileService mediaFileService;

  public List<Survey> getAll() {
    return surveyRepository.findAll();
  }

  public Survey getById(Long id) {
    Survey survey = surveyRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Survey with id " + id + " was not found in database."));
    Set<Song> songs = survey.getSongs();
    songs.forEach(song -> song.setMediaURL(mediaFileService.getMediaPresignedUrl(song.getStorageKey())));

    return survey;
  }

  public Option getOptionById(Long id) {
    return optionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Option with id " + id + " was not found in database."));
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
      throw new IllegalArgumentException("Given survey is NULL, cannot create.");
    }

    survey.setState(SurveyState.CREATED);

    return surveyRepository.save(survey);
  }

  @Transactional
  public void update(Long id, Survey survey) {
    if (survey == null) {
      throw new IllegalArgumentException("Given survey is NULL, cannot update.");
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
      throw new IllegalArgumentException("Cannot add NULL option to a survey.");
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
    for (Vote vote : votes) {
      Long optionId = vote.getOption().getId();
      Option option = getOptionById(optionId);

      vote.setOption(option);
      voteRepository.save(vote);
    }
  }
}
