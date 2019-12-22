package epamers.surwave.entities;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Getter
@Setter
@Table(name = "app_user")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

  public User(Map<String, Object> googleData) {
    this.id = (String) googleData.get("sub");
    this.username = (String) googleData.get("name");
    this.email = (String) googleData.get("email");
    this.locale = (String) googleData.get("locale");
    this.avatarUrl = (String) googleData.get("picture");
  }

  @Id
  private String id;

  @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
  @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
  @Enumerated(EnumType.STRING)
  @Default
  private Set<Role> roles = new HashSet<>();

  private String username;

  private String password;

  private boolean active;

  private String email;

  private String avatarUrl;

  private String locale;

  private LocalDateTime lastVisit;

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
