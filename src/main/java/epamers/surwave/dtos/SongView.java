package epamers.surwave.dtos;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SongView {

  private Long id;

  private String performer;

  private String title;

  private String mediaPath;

  private String comment;
}
