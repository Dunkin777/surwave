package epamers.surwave.converters;

import epamers.surwave.dtos.CompoundResultView;
import epamers.surwave.entities.Features;
import epamers.surwave.entities.Option;
import epamers.surwave.entities.Song;
import epamers.surwave.entities.Vote;
import java.util.Set;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class OptionToSongResultViewConverter implements Converter<Option, CompoundResultView> {

  @Override
  public CompoundResultView convert(Option option) {
    Song song = option.getSong();
    Features features = song.getFeatures();

    Double rating = countFinalRating(option);

    return CompoundResultView.builder()
        .id(song.getId())
        .performer(song.getPerformer())
        .title(song.getTitle())
        .comment(option.getComment())
        .mediaURL(song.getMediaURL())
        .rating(rating)
        .danceability(features.getDanceability())
        .energy(features.getEnergy())
        .valence(features.getValence())
        .build();
  }

  private Double countFinalRating(Option option) {
    Set<Vote> votes = option.getVotes();

    return votes.stream()
        .mapToDouble(Vote::getRating)
        .sum();
  }
}
