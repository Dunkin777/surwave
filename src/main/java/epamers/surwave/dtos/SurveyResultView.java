package epamers.surwave.dtos;

import epamers.surwave.entities.SurveyType;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurveyResultView {

  private Long id;

  @ApiModelProperty(notes = "Type of Survey.")
  private SurveyType type;

  @ApiModelProperty(notes = "Title of whole survey, usually its purpose.")
  private String title;

  @ApiModelProperty(notes = "Message left by Survey creator for every user who wants to vote.")
  private String description;

  @ApiModelProperty(notes = "Collection of results for each participated song.")
  private List<CompoundResultView> songResults;

  @ApiModelProperty(notes = "Sum of ratings of all songs in that survey.")
  private Double ratingsSum;
}
