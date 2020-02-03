package epamers.surwave.entities;

import java.util.Set;
import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "survey_type")
@SuperBuilder
@NoArgsConstructor
@Data
@EqualsAndHashCode(exclude = {"songs"})
public abstract class Survey {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private SurveyType type;

  private String description;

  @Column(nullable = false)
  private Integer proposalsByUser;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private SurveyState state;

  private Boolean isHidden;

  @ManyToMany(cascade = CascadeType.PERSIST)
  @JoinTable(
      name = "survey_user",
      joinColumns = {@JoinColumn(name = "survey_id")},
      inverseJoinColumns = {@JoinColumn(name = "user_id")}
  )
  private Set<User> users;

  @ManyToMany(cascade = CascadeType.PERSIST)
  @JoinTable(
      name = "survey_song",
      joinColumns = {@JoinColumn(name = "survey_id")},
      inverseJoinColumns = {@JoinColumn(name = "song_id")}
  )
  private Set<Song> songs;
}
