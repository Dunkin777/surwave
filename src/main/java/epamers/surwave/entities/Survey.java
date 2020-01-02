package epamers.surwave.entities;

import java.util.Set;
import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "survey")
  @Fetch(FetchMode.JOIN)
  private Set<Song> songs;
}
