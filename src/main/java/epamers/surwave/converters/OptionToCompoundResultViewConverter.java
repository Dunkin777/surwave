package epamers.surwave.converters;

import epamers.surwave.dtos.CompoundResultView;
import epamers.surwave.entities.Features;
import epamers.surwave.entities.Option;
import epamers.surwave.entities.Song;
import epamers.surwave.entities.User;
import epamers.surwave.entities.Vote;
import java.util.Set;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class OptionToCompoundResultViewConverter implements Converter<Option, CompoundResultView> {

  @Override
  public CompoundResultView convert(Option option) {
    Song song = option.getSong();
    User user = option.getUser();
    Double rating = countFinalRating(option);

    CompoundResultView result = CompoundResultView.builder()
        .id(song.getId())
        .performer(song.getPerformer())
        .title(song.getTitle())
        .comment(option.getComment())
        .mediaURL(song.getMediaURL())
        .rating(rating)
        .proposer(user.getUsername())
        .build();

    Features features = song.getFeatures();
    if(features != null) {
      result.setDanceability(features.getDanceability());
      result.setEnergy(features.getEnergy());
      result.setValence(features.getValence());
    }

    return result;
  }

  private Double countFinalRating(Option option) {
    Set<Vote> votes = option.getVotes();

    return votes.stream()
        .mapToDouble(Vote::getRating)
        .sum();
  }
}
