package ma.rest.spring.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import java.util.Date;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/** Compte persisté ; le type est stocké en texte pour rendre la base lisible. */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "compte")
public class Compte {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @PositiveOrZero private double solde;

  @Temporal(TemporalType.DATE)
  @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
  @NotNull
  private Date dateCreation;

  @Enumerated(EnumType.STRING)
  @NotNull
  private TypeCompte type;
}
