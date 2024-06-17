package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.UserAuthEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.repository.UserRepositoryHibernate;
import guru.qa.niffler.model.UserJson;

public class UserHibernateExtension extends CreateUserExtension {
    private final UserRepositoryHibernate userRepositoryHibernate = new UserRepositoryHibernate();

    @Override
    protected UserJson createUser(UserJson user) {
        userRepositoryHibernate.createUserInAuth(UserAuthEntity.fromJson(user));
        userRepositoryHibernate.createInUserData(UserEntity.fromJson(user));
        return user;
    }
}
