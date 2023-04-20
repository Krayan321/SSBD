package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "access_level")
@Inheritance(strategy = InheritanceType.JOINED)
@ToString(callSuper = true)
@NoArgsConstructor
@Getter
@Setter
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "access_level_role")
@NamedQuery(name = "accessLevel.findAll", query = "SELECT o FROM AccessLevel o")
public class AccessLevel extends AbstractEntity implements Serializable {

    public static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(lombok.AccessLevel.NONE)
    private Long id;

    @Getter
    @Enumerated(EnumType.STRING)
    @Setter(lombok.AccessLevel.NONE)
    @Column(name = "access_level_role", nullable = false, updatable = false)
    private Role role;

    @Column(nullable = false, columnDefinition = "boolean default true")
    @NotNull
    private Boolean active;

    @ManyToOne(optional = false)
    @JoinColumn(name = "account_id", referencedColumnName = "id", updatable = false)
    private Account account;

}
