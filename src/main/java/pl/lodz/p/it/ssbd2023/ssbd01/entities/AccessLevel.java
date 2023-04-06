package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@ToString(callSuper = true)
@NoArgsConstructor
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "access_level_role")
public class AccessLevel extends AbstractEntity implements Serializable {

    public static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    @Enumerated(EnumType.STRING)
    @NotNull
    @Basic(optional = false)
    @Setter(lombok.AccessLevel.NONE)
    @Column(name = "access_level_role", nullable = false, updatable = false)
    private Role role;

    @Getter
    @Setter
    private Boolean active = true;

    @ManyToOne
    private Account account;

}
