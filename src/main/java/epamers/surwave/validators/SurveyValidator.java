package epamers.surwave.validators;

import static epamers.surwave.core.ExceptionMessageContract.SURVEY_RESTRICTED_MODIFICATION;

import epamers.surwave.core.exceptions.ValidationException;
import epamers.surwave.entities.ClassicSurvey;
import epamers.surwave.entities.Survey;
import epamers.surwave.entities.SurveyType;
import epamers.surwave.services.SurveyService;
import java.util.Comparator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SurveyValidator implements SurwaveValidator<Survey> {

  private final SurveyService surveyService;

  private static final Comparator<Survey> surveyComparator = Comparator.comparing(Survey::getType)
      .thenComparing(Survey::getProposalsByUser);

  private static final Comparator<ClassicSurvey> classicSurveyComparator = Comparator.comparing(ClassicSurvey::getChoicesByUser)
      .thenComparing(surveyComparator);

  @Override
  public void validate(Survey survey) {
    Survey existingSurvey = surveyService.getById(survey.getId());
    SurveyType type = existingSurvey.getType();

    final int compareResult;

    switch (type) {
      case CLASSIC:
        compareResult = classicSurveyComparator.compare((ClassicSurvey) survey, (ClassicSurvey) existingSurvey);
        break;
      default:
        compareResult = surveyComparator.compare(survey, existingSurvey);
    }
    if (compareResult != 0) {
      throw new ValidationException(SURVEY_RESTRICTED_MODIFICATION);
    }
  }
}
