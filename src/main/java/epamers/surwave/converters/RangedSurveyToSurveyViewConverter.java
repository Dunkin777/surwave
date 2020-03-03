package epamers.surwave.converters;

import epamers.surwave.dtos.SurveyView;
import epamers.surwave.entities.RangedSurvey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RangedSurveyToSurveyViewConverter extends SurveyToSurveyViewConverter implements Converter<RangedSurvey, SurveyView> {

  @Autowired
  public RangedSurveyToSurveyViewConverter(OptionToOptionViewConverter converter) {
    super(converter);
  }

  @Override
  public SurveyView convert(RangedSurvey survey) {
    SurveyView surveyView = super.convert(survey);
    surveyView.setLogarithmicRatingScale(survey.getLogarithmicRatingScale());

    return surveyView;
  }
}