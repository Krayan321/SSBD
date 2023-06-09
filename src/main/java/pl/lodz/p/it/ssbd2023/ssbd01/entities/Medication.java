package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(
    name = "medication",
    indexes = {@Index(name = "category_index", columnList = "category_id", unique = false)})
@ToString
@Getter
@Setter
@NoArgsConstructor
@NamedQuery(name = "medication.findAll", query = "SELECT o FROM Medication o")
@NamedQuery(name = "medication.findByName", query = "SELECT o FROM Medication o WHERE o.name = ?1")
public class Medication extends AbstractEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(lombok.AccessLevel.NONE)
  private Long id;

  @Column(nullable = false, unique = true, name = "medication_name")
  private String name;

  @Column(nullable = false)
  @Min(value = 0, message = "Stock must be greater than or equal to 0")
  private Integer stock;

  @Column(nullable = false)
  @Digits(integer = 10, fraction = 2)
  @Min(value = 0, message = "Current price must be greater than or equal 0")
  private BigDecimal currentPrice;

  @Column(nullable = false)
  @Digits(integer = 10, fraction = 2)
  @Min(value = 0, message = "Previous price must be greater than or equal 0")
  private BigDecimal previousPrice;

  @ManyToOne(
      optional = false,
      cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;

  @Builder
  public Medication(String name, Integer stock, BigDecimal currentPrice, Category category) {
    this.name = name;
    this.stock = stock;
    this.currentPrice = currentPrice;
    this.category = category;
  }
}
