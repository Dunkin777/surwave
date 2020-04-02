package epamers.surwave.services;

import epamers.surwave.core.exceptions.NotAuthenticatedException;
import epamers.surwave.entities.User;
import epamers.surwave.repos.UserRepository;
import java.time.LocalDateTime;
import java.util.Map;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new EntityNotFoundException("User with name " + username + " was not found in database."));
  }

  public User getOrCreateFromGoogleData(Map<String, Object> googleData) {
    String id = (String) googleData.get("sub");
    User user = userRepository.findById(id).orElse(new User(googleData));

    user.setLastVisit(LocalDateTime.now());

    return userRepository.save(user);
  }

  public User getCurrent() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication.getPrincipal() == "anonymousUser") {
      throw new NotAuthenticatedException("Anonymous requests are not supported. Please, use /login");
    }

    return (User) authentication.getPrincipal();
  }

  @Transactional
  public User getById(String id) {
    return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with id " + id + " was not found in database."));
  }

  @Transactional
  public void update(User user) {
    userRepository.save(user);
  }

  @Transactional
  public void save(User user) {
    userRepository.save(user);
  }
}
