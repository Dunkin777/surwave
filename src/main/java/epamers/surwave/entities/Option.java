package epamers.surwave.entities;

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
public class Option {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String author;

  private String title;

  private String mediaUrl;

  private String comment;
}
