package epamers.surwave.converters;

import epamers.surwave.dtos.SurveyView;
import epamers.surwave.entities.ClassicSurvey;
import epamers.surwave.services.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ClassicSurveyToViewConverter extends SurveyToViewConverter implements Converter<ClassicSurvey, SurveyView> {

  @Autowired
  public ClassicSurveyToViewConverter(SongToViewConverter converter, SurveyService surveyService) {
    super(converter, surveyService);
  }

  @Override
  public SurveyView convert(ClassicSurvey survey) {
    SurveyView surveyView = super.convert(survey);
    surveyView.setChoicesByUser(survey.getChoicesByUser());

    return surveyView;
  }
}
