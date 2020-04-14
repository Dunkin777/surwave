package epamers.surwave.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
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

  @JsonIgnore
  private String storageKey;

  @Transient
  private String mediaURL;

  @OneToMany(mappedBy = "song", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private Set<Option> option;

  @Embedded
  private Features features;

  public Set<Survey> getSurveys() {
    return option.stream()
        .map(Option::getSurvey)
        .collect(Collectors.toSet());
  }
}
