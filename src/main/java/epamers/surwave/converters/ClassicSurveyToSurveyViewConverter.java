package epamers.surwave.converters;

import epamers.surwave.dtos.SurveyView;
import epamers.surwave.entities.ClassicSurvey;
import epamers.surwave.entities.Survey;
import epamers.surwave.entities.User;
import epamers.surwave.entities.Vote;
import epamers.surwave.utils.SurveyUtils;
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
    surveyView.setIsVoted(isVoted(survey));

    return surveyView;
  }

  private boolean isVoted(Survey survey) {
    User currentUser = SurveyUtils.getCurrentUser();

    return survey.getVotes()
        .stream()
        .map(Vote::getParticipant)
        .anyMatch(currentUser::equals);
  }
}
