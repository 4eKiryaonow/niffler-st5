package guru.qa.niffler.data.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorityEntity implements Serializable {
    private UUID id;
    private Authority authority;
    private UserEntity user;
}
