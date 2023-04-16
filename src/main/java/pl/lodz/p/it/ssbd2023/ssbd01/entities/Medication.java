package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@ToString
@Getter
@Setter
@NoArgsConstructor
@NamedQuery(name="medication.findAll", query = "SELECT o FROM Medication o")
public class Medication extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @NotNull
    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    @NotNull
    @Min(value = 0, message = "Stock must be greater than or equal to 0")
    private Integer stock;

    @Column(nullable = false)
    @NotNull
    @Digits(integer = 10, fraction = 2)
    @Min(value = 0, message = "Price must be greater than or equal 0")
    private BigDecimal price;

    @Enumerated(value = EnumType.STRING)
    @NotNull
    @Column(nullable = false)
    private Category category;

    @Builder
    public Medication(String name, Integer stock, BigDecimal price,
                      Category category) {
        this.name = name;
        this.stock = stock;
        this.price = price;
        this.category = category;
    }
}
