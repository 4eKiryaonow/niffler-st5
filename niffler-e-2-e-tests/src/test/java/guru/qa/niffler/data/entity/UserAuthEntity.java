package guru.qa.niffler.data.entity;

import guru.qa.niffler.model.UserJson;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
public class UserAuthEntity {
    private UUID id;
    private String username;
    private String password;
    private Boolean enabled;
    private Boolean accountNonExpired;
    private Boolean accountNonLocked;
    private Boolean credentialsNonExpired;
    private List<AuthorityEntity> authorityEntities = new ArrayList<>();

    public static UserAuthEntity fromJson(UserJson userJson) {
        UserAuthEntity userAuthEntity = new UserAuthEntity();
        userAuthEntity.setUsername(userJson.username());
        userAuthEntity.setPassword(userJson.testData().password());
        userAuthEntity.setEnabled(true);
        userAuthEntity.setAccountNonExpired(true);
        userAuthEntity.setAccountNonLocked(true);
        userAuthEntity.setCredentialsNonExpired(true);
        userAuthEntity.setAuthorityEntities(getAllAuthorities());
        return userAuthEntity;
    }

    private static List<AuthorityEntity> getAllAuthorities() {
        return Arrays
                .stream(Authority.values())
                .map(authority -> new AuthorityEntity(null, authority, null))
                .collect(Collectors.toList());
    }
}


