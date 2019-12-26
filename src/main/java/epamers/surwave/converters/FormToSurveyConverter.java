package epamers.surwave.converters;

import epamers.surwave.dtos.SurveyForm;
import epamers.surwave.entities.ClassicSurvey;
import epamers.surwave.entities.RangedSurvey;
import epamers.surwave.entities.Survey;
import epamers.surwave.entities.SurveyType;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class FormToSurveyConverter implements Converter<SurveyForm, Survey> {

  @Override
  public Survey convert(SurveyForm surveyForm) {

    Survey survey;
    if (surveyForm.getType() == SurveyType.CLASSIC) {
      survey = ClassicSurvey.builder()
          .choicesByUser(surveyForm.getChoicesByUser())
          .build();
    } else if (surveyForm.getType() == SurveyType.RANGED) {
      survey = RangedSurvey.builder()
          .logarithmicRatingScale(surveyForm.getLogarithmicRatingScale())
          .build();
    } else {
      throw new IllegalArgumentException("Got unsupported survey type " + surveyForm.getType());
    }

    survey.setDescription(surveyForm.getDescription());
    survey.setProposalsByUser(surveyForm.getProposalsByUser());
    survey.setState(surveyForm.getState());

    return survey;
  }
}
