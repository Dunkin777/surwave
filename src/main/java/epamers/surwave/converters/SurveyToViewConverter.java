package epamers.surwave.converters;

import static java.util.stream.Collectors.toSet;

import epamers.surwave.dtos.OptionView;
import epamers.surwave.dtos.SurveyView;
import epamers.surwave.entities.ClassicSurvey;
import epamers.surwave.entities.RangedSurvey;
import epamers.surwave.entities.Survey;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SurveyToViewConverter implements Converter<Survey, SurveyView> {

  private final OptionToViewConverter converter;

  @Override
  public SurveyView convert(Survey survey) {

    Set<OptionView> options = survey.getOptions().stream()
        .map(converter::convert)
        .collect(toSet());

    SurveyView surveyView = SurveyView.builder()
        .id(survey.getId())
        .type(survey.getType())
        .description(survey.getDescription())
        .options(options)
        .state(survey.getState())
        .proposalsByUser(survey.getProposalsByUser())
        .build();

    if (survey instanceof ClassicSurvey) {
      surveyView.setChoicesByUser(((ClassicSurvey) survey).getChoicesByUser());
    } else if (survey instanceof RangedSurvey) {
      surveyView.setLogarithmicRatingScale(((RangedSurvey) survey).getLogarithmicRatingScale());
    }

    return surveyView;
  }
}
