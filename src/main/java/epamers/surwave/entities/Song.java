package epamers.surwave.entities;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Song {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String performer;

  @Column(nullable = false)
  private String title;

  private String comment;

  private String mediaPath;

  @ManyToMany(mappedBy = "songs")
  private Set<Survey> surveys;

  @ManyToMany(mappedBy = "proposedSongs")
  private Set<User> users;
}
