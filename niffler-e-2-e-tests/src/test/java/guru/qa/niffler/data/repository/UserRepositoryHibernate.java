package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.DataBase;
import guru.qa.niffler.data.entity.UserAuthEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.jpa.EmProvider;
import jakarta.persistence.EntityManager;

import java.util.Optional;
import java.util.UUID;

public class UserRepositoryHibernate implements UserRepository {

    private final EntityManager entityManagerUserData = EmProvider.entityManager(DataBase.USER_DATA);
    private final EntityManager entityManagerAuth = EmProvider.entityManager(DataBase.AUTH);

    @Override
    public UserAuthEntity createUserInAuth(UserAuthEntity userAuth) {
        entityManagerAuth.persist(userAuth);
        return userAuth;
    }

    @Override
    public UserAuthEntity updateUserInAuth(UserAuthEntity userAuth) {
        return entityManagerAuth.merge(userAuth);
    }

    @Override
    public UserEntity createInUserData(UserEntity user) {
        entityManagerUserData.persist(user);
        return user;
    }

    @Override
    public UserEntity updateInUserData(UserEntity user) {
        return entityManagerUserData.merge(user);
    }

    @Override
    public Optional<UserEntity> findUserInUserdataById(UUID id) {
        String hql = "SELECT * FROM \"user\" WHERE id = :id";
        return Optional.ofNullable(
                entityManagerUserData.createQuery(hql, UserEntity.class)
                        .setParameter("id", id).getSingleResult());
    }

    @Override
    public UserAuthEntity findUserInAuth(String username) {
        String hql = "SELECT * FROM \"user\" WHERE username = :username";
        return entityManagerAuth.createQuery(hql, UserAuthEntity.class)
                .setParameter("username", username).getSingleResult();
    }

    @Override
    public UserEntity findUserInUserData(String username) {
        String hql = "SELECT * FROM \"user\" WHERE username = :username";
        return entityManagerUserData.createQuery(hql, UserEntity.class)
                .setParameter("username", username).getSingleResult();
    }
}
