package epamers.surwave.entities;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING)
@SuperBuilder
@NoArgsConstructor
@Data
public abstract class Survey {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private SurveyType type;

  @OneToMany(fetch = FetchType.EAGER)
  private Set<Option> options;

  private String description;

  @Column(nullable = false)
  private Integer proposalsByUser;

  @Column(nullable = false)
  private Boolean isUsersSeparated;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private SurveyState state;
}