package epamers.surwave.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

//  private Long surveyId;

//  @ManyToOne(fetch = FetchType.LAZY)
//  @JoinColumn(name = "surveyId", insertable = false, updatable = false)
//  private Survey survey;
}
