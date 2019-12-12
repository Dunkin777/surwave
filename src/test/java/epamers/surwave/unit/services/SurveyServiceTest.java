package epamers.surwave.unit.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import epamers.surwave.entities.ClassicSurvey;
import epamers.surwave.entities.Option;
import epamers.surwave.entities.Survey;
import epamers.surwave.entities.SurveyState;
import epamers.surwave.repos.SurveyRepository;
import epamers.surwave.services.OptionService;
import epamers.surwave.services.SurveyService;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class SurveyServiceTest {

  @InjectMocks
  SurveyService surveyService;

  @Mock
  OptionService optionService;

  @Mock
  SurveyRepository surveyRepository;

  private final Long OPTION_ID = 156L;
  private final String AUTHOR = "Some Author";
  private final String MEDIA_URL = "http://youtube.com/supervideo256";
  private final String TITLE = "Elton John - Komarinskaya (feat. Ella Fitzgerald)";
  private final String COMMENT = "Starts in D#, then sudden change to another religion.";

  private final Long SURVEY_ID = 35L;
  private final Long NONEXISTENT_SURVEY_ID = 100L;
  private final String SURVEY_DESCRIPTION = "Please think twice before choosing!";
  private Option option;

  private Survey survey;

  @Before
  public void setUp() {

    MockitoAnnotations.initMocks(this);
    option = Option.builder()
        .author(AUTHOR)
        .mediaUrl(MEDIA_URL)
        .title(TITLE)
        .id(OPTION_ID)
        .comment(COMMENT)
        .build();

    Set<Option> options = new HashSet<>();
    options.add(option);

    survey = ClassicSurvey.builder()
        .choicesByUser(3)
        .description(SURVEY_DESCRIPTION)
        .id(SURVEY_ID)
        .proposalsByUser(3)
        .options(options)
        .build();

    when(optionService.process(any())).thenReturn(options);
    when(surveyRepository.findById(SURVEY_ID)).thenReturn(Optional.of(survey));
    when(surveyRepository.findAll()).thenReturn(List.of(survey));
    when(surveyRepository.save(survey)).thenReturn(survey);
    when(surveyRepository.existsById(SURVEY_ID)).thenReturn(true);
  }

  @Test
  public void getAll_success() {

    List<Survey> surveys = surveyService.getAll();

    assertEquals(1, surveys.size());
    assertTrue(surveys.contains(survey));
  }

  @Test
  public void getById_existingId_success() {

    Survey foundSurvey = surveyService.getById(SURVEY_ID);

    assertEquals(survey, foundSurvey);
  }

  @Test(expected = NoSuchElementException.class)
  public void getById_nonexistentID_exception() {

    surveyService.getById(NONEXISTENT_SURVEY_ID);
  }

  @Test
  public void create_validEntity_success() {

    Survey createdSurvey = surveyService.create(survey);

    verify(surveyRepository).save(survey);
    assertEquals(survey, createdSurvey);
    assertEquals(SurveyState.CREATED, survey.getState());
  }

  @Test(expected = IllegalArgumentException.class)
  public void create_nullSurvey_success() {

    surveyService.create(null);
  }

  @Test
  public void update_validArguments_success() {

    surveyService.update(SURVEY_ID, survey);

    verify(surveyRepository).save(survey);
  }

  @Test(expected = NoSuchElementException.class)
  public void update_nonexistentId_exception() {

    surveyService.update(NONEXISTENT_SURVEY_ID, survey);
  }

  @Test(expected = IllegalArgumentException.class)
  public void update_nullSurvey_exception() {

    surveyService.update(SURVEY_ID, null);
  }

  @Test
  public void delete_validId_success() {

    surveyService.delete(SURVEY_ID);

    verify(surveyRepository).deleteById(SURVEY_ID);
  }

  @Test(expected = NoSuchElementException.class)
  public void delete_nonexistentId_exception() {

    surveyService.delete(NONEXISTENT_SURVEY_ID);
  }

  @Test
  public void addOptions_validArguments_success() {

    ArgumentCaptor<Survey> arg = ArgumentCaptor.forClass(Survey.class);
    survey.setOptions(new HashSet<>());

    surveyService.addOptions(SURVEY_ID, List.of(OPTION_ID));

    verify(surveyRepository).save(arg.capture());
    assertEquals(survey, arg.getValue());
    assertTrue(arg.getValue().getOptions().contains(option));
  }

  @Test
  public void updateState_validArguments_success() {

    ArgumentCaptor<Survey> arg = ArgumentCaptor.forClass(Survey.class);

    surveyService.updateState(SURVEY_ID, SurveyState.STOPPED);

    verify(surveyRepository).save(arg.capture());
    assertEquals(survey, arg.getValue());
    assertEquals(SurveyState.STOPPED, arg.getValue().getState());
  }
}