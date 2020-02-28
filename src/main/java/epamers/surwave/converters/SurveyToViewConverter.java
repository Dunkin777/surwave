package epamers.surwave.converters;

import static java.util.stream.Collectors.toSet;

import epamers.surwave.dtos.OptionView;
import epamers.surwave.dtos.SurveyView;
import epamers.surwave.entities.Survey;
import java.util.Set;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class SurveyToViewConverter {

  private final OptionToViewConverter optionToViewConverter;

  public SurveyView convert(Survey survey) {
    Set<OptionView> options = survey.getOptions().stream()
        .map(optionToViewConverter::convert)
        .collect(toSet());

    return SurveyView.builder()
        .id(survey.getId())
        .type(survey.getType())
        .description(survey.getDescription())
        .options(options)
        .state(survey.getState())
        .proposalsByUser(survey.getProposalsByUser())
        .isHidden(survey.getIsHidden())
        .build();
  }
}
