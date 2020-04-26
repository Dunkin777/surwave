package epamers.surwave.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompoundResultView {

  private Long id;

  @ApiModelProperty(notes = "Who performed song on given record. Usually, band or singer name.")
  private String performer;

  @ApiModelProperty(notes = "Name of the composition/track.")
  private String title;

  @ApiModelProperty(notes = "Notes from user who suggested this song for other voters.")
  private String comment;

  @ApiModelProperty(notes = "Path/URL to song media data.")
  private String mediaURL;

  @ApiModelProperty(notes = "Resembles intensity, activity, loudness.")
  private Double energy;

  @ApiModelProperty(notes = "Positivity/happiness level.")
  private Double valence;

  @ApiModelProperty(notes = "How well this track is suited for dance.")
  private Double danceability;

  @ApiModelProperty(notes = "Sum of all votes.")
  private Double rating;
}
