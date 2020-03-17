package epamers.surwave.converters;

import epamers.surwave.dtos.SurveyView;
import epamers.surwave.entities.ClassicSurvey;
import epamers.surwave.entities.Survey;
import epamers.surwave.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    surveyView.setIsVoted(isVoted(survey, surveyView));

    return surveyView;
  }

  private boolean isVoted(Survey survey, SurveyView surveyView) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User currentUser = (User) authentication.getPrincipal();

    return survey.getVotes().stream()
        .filter(vote -> vote.getParticipant().equals(currentUser))
        .count() == surveyView.getChoicesByUser();
  }
}
