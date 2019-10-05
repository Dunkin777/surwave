package epamers.surwave.converters;

import static java.util.stream.Collectors.toSet;

import epamers.surwave.dtos.SurveyForm;
import epamers.surwave.entities.ClassicSurvey;
import epamers.surwave.entities.Option;
import epamers.surwave.entities.RangedSurvey;
import epamers.surwave.entities.Survey;
import epamers.surwave.entities.SurveyType;
import java.util.Set;
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

    Set<Option> options = surveyForm.getOptionIds().stream()
        .map(id -> Option.builder()
            .id(id)
            .build())
        .collect(toSet());

    survey.setOptions(options);
    survey.setDescription(surveyForm.getDescription());
    survey.setIsUsersSeparated(surveyForm.getIsUsersSeparated());
    survey.setProposalsByUser(surveyForm.getProposalsByUser());
    survey.setState(surveyForm.getState());

    return survey;
  }
}
