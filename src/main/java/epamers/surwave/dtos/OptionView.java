package epamers.surwave.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@ApiModel(description = "Option.")
public class OptionView {

  @ApiModelProperty(notes = "Id of the Song that this Option is based on.",
      example = "12")
  private Long songId;

  @ApiModelProperty(notes = "Who performed song on given record. Usually, band or singer name.",
      example = "Elvis Presley")
  private String performer;

  @ApiModelProperty(notes = "Name of the composition/track.", example = "Main theme from Santa Barbara")
  private String title;

  @ApiModelProperty(notes = "Notes from user who suggested this song for other voters.",
      example = "Everybody knows the lyrics, will sing along. But let's play it faster.")
  private String comment;
}
