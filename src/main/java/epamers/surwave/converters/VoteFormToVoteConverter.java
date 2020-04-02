package epamers.surwave.converters;

import epamers.surwave.dtos.VoteForm;
import epamers.surwave.entities.Option;
import epamers.surwave.entities.User;
import epamers.surwave.entities.Vote;
import epamers.surwave.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VoteFormToVoteConverter implements Converter<VoteForm, Vote> {

  private final UserService userService;

  @Override
  public Vote convert(VoteForm voteForm) {
    Option option = Option.builder()
        .id(voteForm.getOptionId())
        .build();

    User user = userService.getCurrent();

    return Vote.builder()
        .option(option)
        .participant(user)
        .rating(voteForm.getRating())
        .build();
  }
}
