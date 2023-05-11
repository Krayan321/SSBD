package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "category")
@ToString
@Getter
@Setter
@NoArgsConstructor
@NamedQuery(name = "category.findAll", query = "SELECT o FROM Category o")
public class Category extends AbstractEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(lombok.AccessLevel.NONE)
  private Long id;

  @NotNull
  @Column(nullable = false, unique = true, name = "category_name")
  private String name;

  @Column(nullable = false, name = "is_on_prescription")
  @NotNull
  private Boolean isOnPrescription;

  @Builder
  public Category(String name, Boolean isOnPrescription) {
    this.name = name;
    this.isOnPrescription = isOnPrescription;
  }
}
