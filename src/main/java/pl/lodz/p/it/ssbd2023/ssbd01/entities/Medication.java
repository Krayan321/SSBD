package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@ToString
@NoArgsConstructor
@NamedQuery(name="medication.findAll", query = "SELECT o FROM Medication o")
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
