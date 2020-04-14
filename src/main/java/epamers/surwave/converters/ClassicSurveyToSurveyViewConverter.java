package epamers.surwave.converters;

import epamers.surwave.dtos.SurveyView;
import epamers.surwave.entities.ClassicSurvey;
import epamers.surwave.entities.User;
import epamers.surwave.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ClassicSurveyToSurveyViewConverter extends SurveyToSurveyViewConverter implements Converter<ClassicSurvey, SurveyView> {

  @Autowired
  public ClassicSurveyToSurveyViewConverter(OptionToOptionViewConverter converter, UserService userService) {
    super(converter, userService);
  }

  @Override
  public SurveyView convert(ClassicSurvey survey) {
    User currentUser = userService.getCurrent();

    SurveyView surveyView = super.convert(survey);
    surveyView.setChoicesByUser(survey.getChoicesByUser());
    surveyView.setIsVoted(survey.isUserVoted(currentUser));

    return surveyView;
  }
}
