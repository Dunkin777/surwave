package epamers.surwave.converters;

import epamers.surwave.dtos.SurveyView;
import epamers.surwave.entities.ClassicSurvey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ClassicSurveyToSurveyViewConverter extends SurveyToSurveyViewConverter implements Converter<ClassicSurvey, SurveyView> {

  @Autowired
  public ClassicSurveyToSurveyViewConverter(OptionToOptionViewConverter converter) {
    super(converter);
  }

  @Override
  public SurveyView convert(ClassicSurvey survey) {
    SurveyView surveyView = super.convert(survey);
    surveyView.setChoicesByUser(survey.getChoicesByUser());

    return surveyView;
  }
}