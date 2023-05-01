package pl.lodz.p.it.ssbd2023.ssbd01.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@ToString
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
public class AccountDTO extends AbstractEntityDTO {

    @Builder
    public AccountDTO(Long id, Long version, String login, Boolean active, Boolean confirmed) {
        super(id, version);
        this.login = login;
        this.active = active;
        this.confirmed = confirmed;
    }

    @NotNull
    private String login;

    @NotNull
    private String email;

    @NotNull
    private Boolean active;

    @NotNull
    private Boolean confirmed;
}
