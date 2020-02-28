package epamers.surwave.converters;

import epamers.surwave.dtos.SurveyView;
import epamers.surwave.entities.RangedSurvey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RangedSurveyToViewConverter extends SurveyToViewConverter implements Converter<RangedSurvey, SurveyView> {

  @Autowired
  public RangedSurveyToViewConverter(OptionToViewConverter converter) {
    super(converter);
  }

  @Override
  public SurveyView convert(RangedSurvey survey) {
    SurveyView surveyView = super.convert(survey);
    surveyView.setLogarithmicRatingScale(survey.getLogarithmicRatingScale());

    return surveyView;
  }
}
