package epamers.surwave.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import epamers.surwave.dtos.UserView;
import epamers.surwave.entities.Role;
import epamers.surwave.entities.User;
import epamers.surwave.services.UserService;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;

public class UserControllerTest {

  private static final String USERNAME = "User Userovich";
  private static final String USER_ID = "38974632876328473";
  private static final String AVATAR_URL = "https://avatar.url";

  @InjectMocks
  private UserController userController;

  @Mock
  private UserService userService;

  @Mock
  private ConversionService converter;

  private UserView userView;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);

    User user = User.builder()
        .avatarUrl(AVATAR_URL)
        .id(USER_ID)
        .username(USERNAME)
        .build();
    user.addRole(Role.USER);

    userView = UserView.builder()
        .avatarUrl(AVATAR_URL)
        .id(USER_ID)
        .username(USERNAME)
        .roles(Set.of(Role.USER))
        .build();

    when(userService.getCurrent()).thenReturn(user);
    when(converter.convert(user, UserView.class)).thenReturn(userView);
  }

  @Test
  public void getUserInfo_success() {
    UserView returnedUser = userController.getInfo();

    assertThat(returnedUser).isEqualTo(userView);
  }
}
