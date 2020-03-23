package epamers.surwave.converters;

import epamers.surwave.dtos.UserView;
import epamers.surwave.entities.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToUserViewConverter implements Converter<User, UserView> {

  @Override
  public UserView convert(User user) {
    return UserView.builder()
        .id(user.getId())
        .username(user.getUsername())
        .avatarUrl(user.getAvatarUrl())
        .roles(user.getRoles())
        .build();
  }
}
