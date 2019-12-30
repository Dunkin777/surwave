package epamers.surwave.dtos;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongForm {

  @NotNull
  private String performer;

  @NotNull
  private String title;

  private String comment;
}
