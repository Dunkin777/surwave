package epamers.surwave.entities;

import static java.util.stream.Collectors.toSet;

import java.util.Collection;
import java.util.Set;
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

  @Column(nullable = false)
  private String title;

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
  private Set<Option> options;

  public Set<Song> getSongs() {
    return options.stream()
        .map(Option::getSong)
        .collect(toSet());
  }

  public Set<Vote> getVotes() {
    return options.stream()
        .map(Option::getVotes)
        .flatMap(Collection::stream)
        .collect(toSet());
  }
}
