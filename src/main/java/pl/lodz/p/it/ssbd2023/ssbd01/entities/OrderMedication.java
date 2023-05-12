package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
    name = "order_medication",
    indexes = {
      @Index(name = "order_index", columnList = "order_id", unique = true),
      @Index(name = "medication_index_om", columnList = "medication_id", unique = true)
    })
@NamedQuery(name = "orderMedication.findAll", query = "SELECT o FROM OrderMedication o")
public class OrderMedication extends AbstractEntity implements Serializable {

  public static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(lombok.AccessLevel.NONE)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "order_id", updatable = false, nullable = false)
  private Order order;

  @ManyToOne(optional = false)
  @JoinColumn(name = "medication_id", updatable = false, nullable = false)
  private Medication medication;

  @NotNull
  @Column(nullable = false)
  @Min(value = 1, message = "Quantity must be greater than 0")
  private Integer quantity;
}
