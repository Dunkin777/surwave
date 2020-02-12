package epamers.surwave.entities;

import java.util.Map;
import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Answer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "survey_id")
  private Survey survey;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ElementCollection
  @CollectionTable(name = "song_rating",
      joinColumns = @JoinColumn(name = "answer_id"))
  @MapKeyJoinColumn(name = "song_id")
  @Column(name = "rating")
  private Map<Song, Integer> votes;
}
