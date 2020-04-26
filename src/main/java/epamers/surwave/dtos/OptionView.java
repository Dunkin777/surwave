package epamers.surwave.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@ApiModel(description = "Option.")
public class OptionView {

  @ApiModelProperty(notes = "Option id.")
  private Long id;

  @ApiModelProperty(notes = "Song that this Option is based on.")
  private SongView song;

  @ApiModelProperty(notes = "Notes from user who suggested this song for other voters.")
  private String comment;
}
