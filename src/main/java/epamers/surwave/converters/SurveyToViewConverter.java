package epamers.surwave.converters;

import static java.util.stream.Collectors.toSet;

import epamers.surwave.dtos.SongView;
import epamers.surwave.dtos.SurveyView;
import epamers.surwave.entities.Survey;
import java.util.Set;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class SurveyToViewConverter {

  private final SongToViewConverter converter;

  public SurveyView convert(Survey survey) {
    Set<SongView> songs = survey.getSongs().stream()
        .map(converter::convert)
        .collect(toSet());

    return SurveyView.builder()
        .id(survey.getId())
        .type(survey.getType())
        .description(survey.getDescription())
        .songs(songs)
        .state(survey.getState())
        .proposalsByUser(survey.getProposalsByUser())
        .isHidden(survey.getIsHidden())
        .build();
  }
}
