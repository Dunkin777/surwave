package epamers.surwave.configuration.security.basic;

import epamers.surwave.entities.User;
import epamers.surwave.repos.UserRepository;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;

@RequiredArgsConstructor
public class BasicUserDetailsManager implements UserDetailsManager {

  private final UserRepository userRepository;

  @Override
  public void createUser(UserDetails user) {
    String username = user.getUsername();
    if (!userExists(username)) {
      userRepository.save((User) user);
    }
  }

  @Override
  public void updateUser(UserDetails user) {
    userRepository.save((User) user);
  }

  @Override
  public void deleteUser(String username) {
    User user = (User) loadUserByUsername(username);
    userRepository.delete(user);
  }

  @Override
  public void changePassword(String oldPassword, String newPassword) {
    throw new UnsupportedOperationException("Cannot change password of OAuth user.");
  }

  @Override
  public boolean userExists(String username) {
    return userRepository.existsByUsername(username);
  }

  @Override
  public UserDetails loadUserByUsername(String username) {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new EntityNotFoundException("User with name " + username + " was not found in database."));
  }
}
