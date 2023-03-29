package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@ToString
@NoArgsConstructor
public class Medication extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private Integer stock;

    @Getter
    @Setter
    private BigDecimal price;

    @Enumerated(value = EnumType.STRING)
    @Getter
    @Setter
    private Category category;
}
