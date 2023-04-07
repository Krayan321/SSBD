package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Medication extends AbstractEntity implements Serializable {

    @Builder
    public Medication(String name, Integer stock, BigDecimal price,
                      Category category) {
        this.name = name;
        this.stock = stock;
        this.price = price;
        this.category = category;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    private String name;

    private Integer stock;

    private BigDecimal price;

    @Enumerated(value = EnumType.STRING)
    private Category category;
}
