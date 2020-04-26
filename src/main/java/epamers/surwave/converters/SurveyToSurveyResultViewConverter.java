package epamers.surwave.converters;

import epamers.surwave.dtos.CompoundResultView;
import epamers.surwave.dtos.SurveyResultView;
import epamers.surwave.entities.Survey;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SurveyToSurveyResultViewConverter implements Converter<Survey, SurveyResultView> {

  private final OptionToSongResultViewConverter optionToSongResultViewConverter;

  @Override
  public SurveyResultView convert(Survey survey) {
    List<CompoundResultView> songResults = survey.getOptions().stream()
        .map(optionToSongResultViewConverter::convert)
        .collect(Collectors.toList());

    Double ratingsSum = songResults.stream()
        .mapToDouble(CompoundResultView::getRating)
        .sum();

    return SurveyResultView.builder()
        .id(survey.getId())
        .title(survey.getTitle())
        .description(survey.getDescription())
        .songResults(songResults)
        .ratingsSum(ratingsSum)
        .build();
  }
}
