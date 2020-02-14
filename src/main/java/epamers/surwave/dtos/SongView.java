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

  @ApiModelProperty(notes = "Band or Person name by whom this song was performed.",
      example = "John Lemon")
  private String performer;

  @ApiModelProperty(notes = "Track name.", example = "Main theme from Santa Barbara")
  private String title;

  @ApiModelProperty(notes = "Name of media file uploaded for this song.", example = "song4u.mp3")
  private String mediaPath;

  @ApiModelProperty(notes = "Message from user who suggested this song for everyone who might vote "
      + "for it.", example = "I love this song! Let's play it 2 times faster!")
  private String comment;
}
