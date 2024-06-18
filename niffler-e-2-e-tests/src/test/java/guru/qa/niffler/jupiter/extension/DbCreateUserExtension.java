package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.UserAuthEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.repository.UserRepositoryImplSpringJdbc;
import guru.qa.niffler.model.UserJson;

public class DbCreateUserExtension extends CreateUserExtension {

    private final UserRepositoryImplSpringJdbc userRepositoryImplSpringJdbc = new UserRepositoryImplSpringJdbc();

    @Override
    protected UserJson createUser(UserJson user) {
        userRepositoryImplSpringJdbc.createUserInAuth(UserAuthEntity.fromJson(user));
        userRepositoryImplSpringJdbc.createInUserData(UserEntity.fromJson(user));
        return user;
    }
}
