package epamers.surwave.entities;

import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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

  @OneToMany(mappedBy = "song", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private Set<SurveyUserSongLink> surveyUserSongLink;

  public Set<Survey> getSurveys() {
    return surveyUserSongLink.stream()
        .map(SurveyUserSongLink::getSurvey)
        .collect(Collectors.toSet());
  }
}
