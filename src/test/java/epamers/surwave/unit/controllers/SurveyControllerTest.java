package epamers.surwave.unit.controllers;

import static epamers.surwave.core.Contract.SURVEY_URL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import epamers.surwave.controllers.SurveyController;
import epamers.surwave.dtos.OptionForm;
import epamers.surwave.dtos.SurveyForm;
import epamers.surwave.dtos.SurveyResultView;
import epamers.surwave.dtos.SurveyView;
import epamers.surwave.dtos.VoteForm;
import epamers.surwave.entities.ClassicSurvey;
import epamers.surwave.entities.Option;
import epamers.surwave.entities.Survey;
import epamers.surwave.entities.SurveyType;
import epamers.surwave.entities.User;
import epamers.surwave.entities.Vote;
import epamers.surwave.services.SurveyService;
import epamers.surwave.validators.SurwaveValidator;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;

public class SurveyControllerTest {

  private static final Long OPTION_ID = 55L;
  private static final Long SURVEY_ID = 77L;

  @InjectMocks
  SurveyController surveyController;

  @Mock
  HttpServletResponse response;

  @Mock
  private SurveyService surveyService;

  @Mock
  private ConversionService converter;

  @Mock
  private SurwaveValidator surwaveValidator;

  private Survey survey;
  private Vote vote;
  private List<Survey> surveys;
  private SurveyForm surveyForm;
  private SurveyView surveyView;
  private VoteForm voteForm;
  private List<VoteForm> voteForms;
  private User currentUser;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);

    currentUser = new User();

    surveyForm = SurveyForm.builder()
        .build();

    voteForm = VoteForm.builder()
        .build();

    voteForms = List.of(voteForm);

    surveyView = SurveyView.builder()
        .id(SURVEY_ID)
        .type(SurveyType.CLASSIC)
        .build();

    survey = ClassicSurvey.builder()
        .id(SURVEY_ID)
        .isHidden(false)
        .build();

    Survey hiddenSurvey = ClassicSurvey.builder()
        .isHidden(true)
        .build();

    vote = Vote.builder()
        .build();

    surveys = List.of(survey, hiddenSurvey);

    when(surveyService.getAll()).thenReturn(surveys);
    when(surveyService.getByIdWithSongURLs(SURVEY_ID)).thenReturn(survey);
    when(surveyService.getById(SURVEY_ID)).thenReturn(survey);
    when(surveyService.create(survey)).thenReturn(survey);
    when(converter.convert(survey, SurveyView.class)).thenReturn(surveyView);
    when(converter.convert(hiddenSurvey, SurveyView.class)).thenReturn(surveyView);
    when(converter.convert(surveyForm, Survey.class)).thenReturn(survey);
    when(converter.convert(voteForm, Vote.class)).thenReturn(vote);
  }

  @Test
  public void getAll_success() {
    List<SurveyView> returnedSurveys = surveyController.getAll();

    assertEquals(2, returnedSurveys.size());
    assertTrue(returnedSurveys.contains(surveyView));
  }

  @Test
  public void get_success() {
    SurveyView returnedSurvey = surveyController.get(SURVEY_ID);

    assertEquals(surveyView, returnedSurvey);
  }

  @Test
  public void create_success() {
    surveyController.create(surveyForm, response);

    verify(surveyService).create(survey);
    verify(response).addHeader("Location", SURVEY_URL + "/" + SURVEY_ID);
  }

  @Test
  public void update_success() {
    surveyController.update(SURVEY_ID, surveyForm);

    verify(surwaveValidator).validate(survey);
    verify(surveyService).update(survey);
  }

  @Test
  public void removeOption_success() {
    surveyController.removeOption(OPTION_ID);

    verify(surveyService).removeOption(OPTION_ID);
  }

  @Test
  public void addVotes_success() {
    surveyController.addVotes(SURVEY_ID, voteForms);

    verify(surwaveValidator).validate(voteForms);
    verify(surveyService).addVotes(List.of(vote));
  }

  @Test
  public void getAllFiltered_success() {
    when(surveyService.getAllFiltered(currentUser)).thenReturn(surveys);

    List<SurveyView> returnedSurveys = surveyController.getAllFiltered(currentUser);

    assertEquals(2, returnedSurveys.size());
  }

  @Test
  public void getByIdFiltered_success() {
    when(surveyService.getByIdFiltered(SURVEY_ID, currentUser)).thenReturn(survey);

    SurveyView returnedSurveys = surveyController.getFiltered(currentUser, SURVEY_ID);

    assertEquals(surveyView, returnedSurveys);
  }

  @Test
  public void addOption_success() {
    OptionForm optionForm = OptionForm.builder()
        .build();
    Option option = Option.builder()
        .id(OPTION_ID)
        .build();
    when(converter.convert(optionForm, Option.class)).thenReturn(option);
    when(surveyService.addOption(SURVEY_ID, option, currentUser)).thenReturn(option);

    surveyController.addOption(currentUser, SURVEY_ID, optionForm, response);

    verify(response).addHeader("Location", SURVEY_URL + "/" + OPTION_ID);
  }

  @Test
  public void getResult_success() {
    SurveyResultView surveyResultView = SurveyResultView.builder().build();
    when(converter.convert(survey, SurveyResultView.class)).thenReturn(surveyResultView);
    when(surveyService.getByIdForRating(SURVEY_ID)).thenReturn(survey);

    SurveyResultView returnedResult = surveyController.getResult(SURVEY_ID);

    assertThat(returnedResult).isEqualTo(surveyResultView);
  }
}
