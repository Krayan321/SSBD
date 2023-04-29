package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@ToString
@Getter
@Table(name = "account")
@Setter
@NamedQuery(name="account.findAll", query = "SELECT o FROM Account o")
@NamedQuery(name="account.findByLogin", query = "SELECT o FROM Account o WHERE o.login = ?1")
public class Account extends AbstractEntity implements Serializable {

    public static final long serialVersionUID = 1L;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE}, mappedBy = "account")
    @ToString.Exclude
    Set<AccessLevel> accessLevels = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(lombok.AccessLevel.NONE)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotNull
    private String login;

    @NotNull
    @Column(unique = true, nullable = false)
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$")
    private String email;

    @ToString.Exclude
    @Column(nullable = false)
    @Pattern(regexp = "^[0-9a-f]{64}$")
    private String password;

    @NotNull
    @Column(nullable = false)
    private Boolean active;

    @NotNull
    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean confirmed;

    @NotNull
    @Basic(optional = false)
    private Locale language;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_positive_login")
    private Date lastPositiveLogin;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_negative_login")
    private Date lastNegativeLogin;

    @Column(name = "logical_address")
    private String logicalAddress;

    @Builder
    public Account(String login, String password) {
        this.login = login;
        this.password = password;
    }

}
