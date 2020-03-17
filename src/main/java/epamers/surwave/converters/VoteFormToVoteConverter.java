package epamers.surwave.converters;

import epamers.surwave.dtos.VoteForm;
import epamers.surwave.entities.Vote;
import epamers.surwave.repos.OptionRepository;
import epamers.surwave.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VoteFormToVoteConverter implements Converter<VoteForm, Vote> {

  private final UserService userService;
  private final OptionRepository optionRepository;

  @Override
  public Vote convert(VoteForm voteForm) {
    return Vote.builder()
        .option(optionRepository.getOne(voteForm.getOptionId()))
        .participant(userService.getById(voteForm.getParticipantId()))
        .rating(voteForm.getRating())
        .build();
  }
}
