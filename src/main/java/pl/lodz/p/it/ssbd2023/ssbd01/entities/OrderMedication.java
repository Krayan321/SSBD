package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
public class OrderMedication extends AbstractEntity implements Serializable {

    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @ManyToOne
    @Getter
    @Setter
    private Order order;

    @OneToOne
    @Getter
    @Setter
    private Medication medication;

    @Getter
    @Setter
    private Integer quantity;

}
