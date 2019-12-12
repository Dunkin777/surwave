package epamers.surwave.entities;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "app_user")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

  @Id
  private String id;

  private String name;

  private String email;

  private String userpic;

  private String locale;

  private LocalDateTime lastVisit;
}
