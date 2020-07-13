package epamers.surwave.unit.validators;

import static epamers.surwave.core.ExceptionMessageContract.SURVEY_RESTRICTED_MODIFICATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.when;

import epamers.surwave.TestUtils;
import epamers.surwave.core.exceptions.ValidationException;
import epamers.surwave.entities.ClassicSurvey;
import epamers.surwave.entities.Survey;
import epamers.surwave.entities.SurveyType;
import epamers.surwave.services.SurveyService;
import epamers.surwave.validators.SurveyValidator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class SurveyValidatorTest {

  @Mock
  private SurveyService surveyService;

  @InjectMocks
  private SurveyValidator surveyValidator;

  private Survey newSurvey;
  private Survey existingSurvey;

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);

    newSurvey = TestUtils.getValidClassicSurvey();
    existingSurvey = TestUtils.getValidClassicSurvey();

    when(surveyService.getById(newSurvey.getId())).thenReturn(existingSurvey);
  }

  @Test
  public void change_title_success() {
    newSurvey.setTitle("new awesome title");
    surveyValidator.validate(newSurvey);
  }

  @Test
  public void change_description_success() {
    newSurvey.setDescription("new awesome description");
    surveyValidator.validate(newSurvey);
  }

  @Test
  public void change_type_exception() {
    newSurvey.setType(SurveyType.RANGED);

    checkForException();
  }

  @Test
  public void change_proposalsByUser_exception() {
    newSurvey.setProposalsByUser(42);

    checkForException();
  }

  @Test
  public void change_choicesByUser_exception() {
    ((ClassicSurvey) newSurvey).setChoicesByUser(42);

    checkForException();
  }

  private void checkForException() {
    Throwable thrown = catchThrowable(() -> surveyValidator.validate(newSurvey));

    assertThat(thrown).isInstanceOf(ValidationException.class)
        .hasMessage(SURVEY_RESTRICTED_MODIFICATION);
  }
}
