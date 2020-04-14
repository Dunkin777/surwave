package epamers.surwave.converters;

import epamers.surwave.dtos.SurveyView;
import epamers.surwave.entities.RangedSurvey;
import epamers.surwave.entities.User;
import epamers.surwave.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RangedSurveyToSurveyViewConverter extends SurveyToSurveyViewConverter implements Converter<RangedSurvey, SurveyView> {

  @Autowired
  public RangedSurveyToSurveyViewConverter(OptionToOptionViewConverter converter, UserService userService) {
    super(converter, userService);
  }

  @Override
  public SurveyView convert(RangedSurvey survey) {
    User currentUser = userService.getCurrent();

    SurveyView surveyView = super.convert(survey);
    surveyView.setLogarithmicRatingScale(survey.getLogarithmicRatingScale());
    surveyView.setIsVoted(survey.isUserVoted(currentUser));

    return surveyView;
  }
}
