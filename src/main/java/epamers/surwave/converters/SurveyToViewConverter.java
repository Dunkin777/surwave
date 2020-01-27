package epamers.surwave.converters;

import static java.util.stream.Collectors.toSet;

import epamers.surwave.dtos.SongView;
import epamers.surwave.dtos.SurveyView;
import epamers.surwave.entities.Song;
import epamers.surwave.entities.Survey;
import epamers.surwave.entities.User;
import epamers.surwave.services.SurveyService;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@RequiredArgsConstructor
public abstract class SurveyToViewConverter {

  private final SongToViewConverter songConverter;
  private final SurveyService surveyService;

  public SurveyView convert(Survey survey) {
    Set<SongView> songs = survey.getSongs().stream()
        .map(songConverter::convert)
        .collect(toSet());

    Map<Song, User> songUserMapBySurveyId = surveyService.getSongUserMapBySurveyId(survey.getId());

    return SurveyView.builder()
        .id(survey.getId())
        .type(survey.getType())
        .description(survey.getDescription())
        .songs(songs)
        .state(survey.getState())
        .proposalsByUser(survey.getProposalsByUser())
        .isHidden(survey.getIsHidden())
        .songToUserID(songUserMapBySurveyId.entrySet().stream().collect(Collectors.toMap(
            entry -> (SongView) songConverter.convert(entry.getKey()),
            entry -> (String) entry.getValue().getId()
        )))
        .build();
  }
}
