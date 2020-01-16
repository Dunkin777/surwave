package epamers.surwave.services;

import epamers.surwave.entities.User;
import epamers.surwave.repos.UserRepository;
import java.time.LocalDateTime;
import java.util.Map;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
    return userRepository.findByUsername(username).orElseThrow();
  }

  public User getOrCreateFromGoogleData(Map<String, Object> googleData) {
    String id = (String) googleData.get("sub");
    User user = userRepository.findById(id).orElse(new User(googleData));

    user.setLastVisit(LocalDateTime.now());
    return userRepository.save(user);
  }

  @Transactional
  public User getById(String id) {
    return userRepository.findById(id).orElseThrow();
  }

  @Transactional
  public void update(User user) {
    userRepository.save(user);
  }
}
