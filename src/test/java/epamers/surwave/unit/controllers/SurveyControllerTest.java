package epamers.surwave.unit.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import epamers.surwave.controllers.SurveyController;
import epamers.surwave.dtos.SurveyForm;
import epamers.surwave.dtos.SurveyView;
import epamers.surwave.entities.ClassicSurvey;
import epamers.surwave.entities.Survey;
import epamers.surwave.entities.SurveyType;
import epamers.surwave.services.SurveyService;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;

public class SurveyControllerTest {

  @InjectMocks
  SurveyController surveyController;

  @Mock
  private SurveyService surveyService;

  @Mock
  private ConversionService converter;

  @Mock
  HttpServletResponse response;

  private final Long OPTION_ID = 55L;
  private final Long SURVEY_ID = 77L;
  private Survey survey;
  private List<Survey> surveys;
  private SurveyForm surveyForm;
  private SurveyView surveyView;

  @Before
  public void setUp() {

    MockitoAnnotations.initMocks(this);

    surveyForm = SurveyForm.builder()
        .build();

    surveyView = SurveyView.builder()
        .id(SURVEY_ID)
        .type(SurveyType.CLASSIC)
        .build();

    survey = ClassicSurvey.builder()
        .id(SURVEY_ID)
        .build();

    surveys = List.of(survey);

    when(surveyService.getAll()).thenReturn(surveys);
    when(surveyService.getById(SURVEY_ID)).thenReturn(survey);
    when(surveyService.create(survey)).thenReturn(survey);
    when(converter.convert(survey, SurveyView.class)).thenReturn(surveyView);
    when(converter.convert(surveyForm, Survey.class)).thenReturn(survey);
  }

  @Test
  public void getAllSurveys_success() {

    List<SurveyView> returnedSurveys = surveyController.getAllSurveys();

    assertEquals(1, returnedSurveys.size());
    assertTrue(returnedSurveys.contains(surveyView));
  }

  @Test
  public void getSurvey_success() {

    SurveyView returnedSurvey = surveyController.getSurvey(SURVEY_ID);

    assertEquals(surveyView, returnedSurvey);
  }

  @Test
  public void createSurvey_success() {

    surveyController.createSurvey(surveyForm, response);

    verify(surveyService).create(survey);
    verify(response).addHeader(any(), any());
  }

  @Test
  public void updateSurvey_success() {

    surveyController.updateSurvey(SURVEY_ID, surveyForm);

    verify(surveyService).update(SURVEY_ID, survey);
  }

  @Test
  public void addOptionsToSurvey_success() {

    surveyController.addOptionsToSurvey(SURVEY_ID, List.of(OPTION_ID));

    verify(surveyService).addOptions(SURVEY_ID, List.of(OPTION_ID));
  }

  @Test
  public void deleteSurvey_success() {

    surveyController.deleteSurvey(SURVEY_ID);

    verify(surveyService).delete(SURVEY_ID);
  }
}