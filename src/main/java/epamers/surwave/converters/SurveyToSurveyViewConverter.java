package epamers.surwave.converters;

import static java.util.stream.Collectors.toSet;

import epamers.surwave.dtos.OptionView;
import epamers.surwave.dtos.SurveyView;
import epamers.surwave.entities.Survey;
import epamers.surwave.services.UserService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public abstract class SurveyToSurveyViewConverter {

  private final OptionToOptionViewConverter optionToOptionViewConverter;
  protected final UserService userService;

  public SurveyView convert(Survey survey) {
    Set<OptionView> options = survey.getOptions().stream()
        .map(optionToOptionViewConverter::convert)
        .collect(toSet());

    return SurveyView.builder()
        .id(survey.getId())
        .type(survey.getType())
        .title(survey.getTitle())
        .description(survey.getDescription())
        .options(shuffle(options))
        .state(survey.getState())
        .proposalsByUser(survey.getProposalsByUser())
        .isHidden(survey.getIsHidden())
        .build();
  }

  private Set<OptionView> shuffle(Set<OptionView> options) {
    List<OptionView> optionList = new ArrayList<>(options);
    Collections.shuffle(optionList);

    return new HashSet<>(optionList);
  }
}
