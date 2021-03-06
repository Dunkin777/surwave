package epamers.surwave.services;

import static epamers.surwave.core.ExceptionMessageContract.USER_NOT_AUTHENTICATED;
import static epamers.surwave.core.ExceptionMessageContract.USER_NOT_FOUND_BY_ID;
import static epamers.surwave.core.ExceptionMessageContract.USER_NOT_FOUND_BY_NAME;

import epamers.surwave.core.exceptions.NotAuthenticatedException;
import epamers.surwave.entities.User;
import epamers.surwave.repos.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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
        .orElseThrow(() -> new EntityNotFoundException(String.format(USER_NOT_FOUND_BY_NAME, username)));
  }

  public User getOrCreateFromGoogleData(Map<String, Object> googleData) {
    String id = (String) googleData.get("sub");
    User user = userRepository.findById(id).orElse(new User(googleData));

    user.setLastVisit(LocalDateTime.now());

    return userRepository.save(user);
  }

  public List<GrantedAuthority> extractAuthorities(Map<String, Object> map){
    String id = (String) map.get("sub");
    Optional<User> optional = userRepository.findById(id);

    if (optional.isPresent()) {
      User user = optional.get();

      return new ArrayList<>(user.getAuthorities());
    }

    return Collections.emptyList();
  }

  public User getCurrent() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication.getPrincipal() == "anonymousUser") {
      throw new NotAuthenticatedException(USER_NOT_AUTHENTICATED);
    }

    return (User) authentication.getPrincipal();
  }

  @Transactional
  public User getById(String id) {
    return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format(USER_NOT_FOUND_BY_ID, id)));
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
