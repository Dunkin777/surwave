package epamers.surwave.dtos;

import epamers.surwave.entities.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Set;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@ApiModel(description = "User information")
public class UserView {

  @ApiModelProperty(notes = "Google account's id.", example = "1942578382034356")
  private String id;

  @ApiModelProperty(notes = "User's name in his google account.", example = "Elvis Presley")
  private String username;

  @ApiModelProperty(notes = "Url of user's google account photo.", example = "https://lh0.googleusercontent.com/a-/AAAAAAA")
  private String avatarUrl;

  @ApiModelProperty(notes = "Access level. USER or ADMIN.", example = "USER")
  private Set<Role> roles;
}
