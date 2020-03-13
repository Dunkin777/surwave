package epamers.surwave.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Resembles one Song that can be used as voting option in some survey.")
public class SongForm {

  @NotNull
  @ApiModelProperty(required = true, notes = "Who performed song on given record. Usually, band or singer name.", example = "John Lennon")
  private String performer;

  @NotNull
  @ApiModelProperty(required = true, notes = "Name of the composition/track.", example = "Fly As A Bird")
  private String title;

  @NotNull
  @ApiModelProperty(required = true, notes = "File", example = "John Lennon")
  private MultipartFile mediaFile;
}
