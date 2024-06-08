package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.UserAuthEntity;
import guru.qa.niffler.data.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    UserAuthEntity createUserInAuth(UserAuthEntity userAuth);

    UserAuthEntity updateUserInAuth(UserAuthEntity userAuth);

    UserEntity createInUserData(UserEntity user);

    UserEntity updateInUserData(UserEntity user);

    Optional<UserEntity> findUserInUserdataById(UUID id);
}
