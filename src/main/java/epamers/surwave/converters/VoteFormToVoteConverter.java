package epamers.surwave.converters;

import epamers.surwave.dtos.VoteForm;
import epamers.surwave.entities.Option;
import epamers.surwave.entities.User;
import epamers.surwave.entities.Vote;
import epamers.surwave.utils.SurveyUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class VoteFormToVoteConverter implements Converter<VoteForm, Vote> {

  @Override
  public Vote convert(VoteForm voteForm) {
    Option option = Option.builder()
        .id(voteForm.getOptionId())
        .build();

    User user = SurveyUtils.getCurrentUser();

    return Vote.builder()
        .option(option)
        .participant(user)
        .rating(voteForm.getRating())
        .build();
  }
}
