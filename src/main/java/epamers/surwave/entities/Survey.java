package epamers.surwave.entities;

import java.util.Set;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import lombok.Data;

@MappedSuperclass
@Data
abstract class Survey {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @OneToMany(fetch = FetchType.EAGER)
  private Set<Option> options;

  @Enumerated(EnumType.STRING)
  private SurveyType type;

  private Integer proposalsByUser;

  private String description;

  private Boolean isStarted;
}
