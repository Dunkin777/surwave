package epamers.surwave.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@ApiModel(description = "Song properties.")
public class FeaturesDto {

  @ApiModelProperty(notes = "How well this track is suited for dance.")
  private Double danceability;

  @ApiModelProperty(notes = "Resembles intensity, activity, loudness.")
  private Double energy;

  @ApiModelProperty(notes = "Positivity/happiness level.")
  private Double valence;
}
