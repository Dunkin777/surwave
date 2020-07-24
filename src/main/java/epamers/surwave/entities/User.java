package epamers.surwave.entities;

import static java.util.stream.Collectors.toSet;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Getter
@Setter
@Builder
@Table(name = "app_user")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "username", "email"})
public class User implements UserDetails {

  @Id
  private String id;

  @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<UserRole> userRoles;

  @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private Set<Option> options;

  private String username;
  private String password;
  private Boolean active;
  private String email;
  private String avatarUrl;
  private String locale;
  private LocalDateTime lastVisit;

  public User(Map<String, Object> googleData) {
    this.id = (String) googleData.get("sub");
    this.username = (String) googleData.get("name");
    this.email = (String) googleData.get("email");
    this.locale = (String) googleData.get("locale");
    this.avatarUrl = (String) googleData.get("picture");
    userRoles = new HashSet<>();
    active = true;
    addRole(Role.USER);
  }

  public Set<Song> getProposedSongs() {
    return options.stream()
        .map(Option::getSong)
        .collect(toSet());
  }

  @Override
  public Set<? extends GrantedAuthority> getAuthorities() {

    return userRoles.stream()
        .map(UserRole::getRole)
        .map(Role::getAuthorityTitle)
        .map(SimpleGrantedAuthority::new)
        .collect(toSet());
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return active;
  }

  public boolean isAdmin() {
    return userRoles.stream()
        .map(UserRole::getRole)
        .anyMatch(role -> role.equals(Role.ADMIN));
  }

  public void addRole(Role role) {
    if (userRoles == null) {
      userRoles = new HashSet<>();
    }

    UserRole userRole = UserRole.builder()
        .role(role)
        .user(this)
        .build();

    userRoles.add(userRole);
  }

  public Set<Role> getRoles() {
    return userRoles.stream()
        .map(UserRole::getRole)
        .collect(toSet());
  }
}
