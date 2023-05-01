package pl.lodz.p.it.ssbd2023.ssbd01.dto;

import lombok.*;

@ToString
@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractEntityDTO {

    private Long id;

    private Long version;

}
