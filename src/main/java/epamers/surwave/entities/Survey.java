package epamers.surwave.entities;

import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "survey_type")
@SuperBuilder
@NoArgsConstructor
@Data
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

  @OneToMany(mappedBy = "survey", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private Set<SurveyUserSong> surveyUserSongs;

  public void addSong(SurveyUserSong songLink) {
    surveyUserSongs.add(songLink);
  }

  public Set<Song> getSongs() {
    return surveyUserSongs.stream()
        .map(SurveyUserSong::getSong)
        .collect(Collectors.toSet());
  }

  public Set<Vote> getVotes() {
    return surveyUserSongs.stream()
        .flatMap(surveyUserSong -> surveyUserSong.getVotes().stream())
        .collect(Collectors.toSet());
  }
}
