package epamers.surwave.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Resembles one voting option in some survey. Based on certain song.")
public class OptionForm {

  @NotNull
  @ApiModelProperty(required = true, notes = "Id of the Song that this Option is based on.",
      example = "12")
  private Long songId;

  @Size(max = 2000)
  @ApiModelProperty(notes = "Notes from user who suggested this song for other voters.",
      example = "Everybody knows the lyrics, will sing along. But let's play it faster.")
  private String comment;
}
