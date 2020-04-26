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

  @ApiModelProperty(notes = "Google account's id.")
  private String id;

  @ApiModelProperty(notes = "User's name in his google account.")
  private String username;

  @ApiModelProperty(notes = "Url of user's google account photo.")
  private String avatarUrl;

  @ApiModelProperty(notes = "Access level. USER or ADMIN.")
  private Set<Role> roles;
}
