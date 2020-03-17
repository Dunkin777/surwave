package epamers.surwave.entities;

import static java.util.stream.Collectors.toSet;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Getter
@Setter
@Table(name = "app_user")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "username", "email"})
public class User implements UserDetails {

  public User(Map<String, Object> googleData) {
    this.id = (String) googleData.get("sub");
    this.username = (String) googleData.get("name");
    this.email = (String) googleData.get("email");
    this.locale = (String) googleData.get("locale");
    this.avatarUrl = (String) googleData.get("picture");
    this.roles.add(Role.USER);
  }

  @Id
  private String id;

  @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
  @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
  @Enumerated(EnumType.STRING)
  private Set<Role> roles = new HashSet<>();

  @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private Set<Option> options;

  private String username;

  private String password;

  private Boolean active = true;

  private String email;

  private String avatarUrl;

  private String locale;

  private LocalDateTime lastVisit;

  public Set<Song> getProposedSongs() {
    return options.stream()
        .map(Option::getSong)
        .collect(toSet());
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles;
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
}
