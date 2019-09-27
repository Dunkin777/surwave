package epamers.surwave.dtos;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OptionView {

  private Long id;

  private String author;

  private String title;

  private String mediaUrl;

  private String comment;
}
