package epamers.surwave.converters;

import static java.util.stream.Collectors.toList;

import epamers.surwave.dtos.OptionView;
import epamers.surwave.dtos.SurveyView;
import epamers.surwave.entities.Survey;
import epamers.surwave.services.UserService;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public abstract class SurveyToSurveyViewConverter {

  private final OptionToOptionViewConverter optionToOptionViewConverter;
  protected final UserService userService;

  public SurveyView convert(Survey survey) {
    List<OptionView> options = survey.getOptions().stream()
        .map(optionToOptionViewConverter::convert)
        .sorted(Comparator.comparing(s -> s.getSong().getPerformer()))
        .collect(toList());

    return SurveyView.builder()
        .id(survey.getId())
        .type(survey.getType())
        .title(survey.getTitle())
        .description(survey.getDescription())
        .options(options)
        .state(survey.getState())
        .proposalsByUser(survey.getProposalsByUser())
        .isHidden(survey.getIsHidden())
        .build();
  }
}
