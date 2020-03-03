package epamers.surwave.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@ApiModel(description = "Song. Nothing more to say.")
public class SongView {

  private Long id;

  @ApiModelProperty(notes = "Who performed song on given record. Usually, band or singer name.",
      example = "Elvis Presley")
  private String performer;

  @ApiModelProperty(notes = "Name of the composition/track.", example = "Main theme from Santa Barbara")
  private String title;

  @ApiModelProperty(required = true, notes = "Path/URL to song media data.")
  private String mediaPath;
}
