package epamers.surwave.entities;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.*;
import lombok.AllArgsConstructor;
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

  @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
  @JoinTable(
      name = "user_song",
      joinColumns = {@JoinColumn(name = "user_id")},
      inverseJoinColumns = {@JoinColumn(name = "song_id")}
  )
  private Set<Song> proposedSongs;

  @ManyToMany(mappedBy = "users")
  private Set<Survey> surveys;

  private String username;

  private String password;

  private Boolean active = true;

  private String email;

  private String avatarUrl;

  private String locale;

  private LocalDateTime lastVisit;

  public Set<Song> getSongsBySurvey(Survey survey) {
    return proposedSongs.stream().filter(song -> song.getSurveys().contains(survey)).collect(Collectors.toSet());
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
