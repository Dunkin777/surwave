package epamers.surwave.controllers;

import static epamers.surwave.core.Contract.USER_URL;

import epamers.surwave.dtos.UserView;
import epamers.surwave.entities.User;
import epamers.surwave.services.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(USER_URL)
public class UserController {

  private final UserService userService;
  private final ConversionService converter;

  @GetMapping("/info")
  @ApiOperation(
      value = "Get current user info",
      notes = "Returns information about current user. Returns 401th code if user is not logged in."
  )
  public UserView getInfo() {
    User user = userService.getCurrentUser();

    return converter.convert(user, UserView.class);
  }
}
