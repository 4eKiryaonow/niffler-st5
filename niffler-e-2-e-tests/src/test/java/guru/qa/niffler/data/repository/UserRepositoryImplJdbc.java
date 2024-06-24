package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.DataSourceProvider;
import guru.qa.niffler.data.entity.AuthorityEntity;
import guru.qa.niffler.data.entity.UserAuthEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.model.CurrencyValues;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.DataBase.AUTH;
import static guru.qa.niffler.data.DataBase.USER_DATA;

public class UserRepositoryImplJdbc implements UserRepository {

    private static final DataSource userAuthDataSource = DataSourceProvider.dataSource(AUTH);
    private static final DataSource userDataSource = DataSourceProvider.dataSource(USER_DATA);
    private static final PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    public UserAuthEntity createUserInAuth(UserAuthEntity userAuth) {
        try (Connection connection = userAuthDataSource.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement psUser = connection.prepareStatement(
                    "INSERT INTO \"user\" (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) VALUES(?, ?, ?, ?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS);
                 PreparedStatement psAuthority = connection.prepareStatement(
                         "INSERT INTO authority (user_id, authority) VALUES(?, ?)")) {
                psUser.setString(1, userAuth.getUsername());
                psUser.setString(2, encoder.encode(userAuth.getPassword()));
                psUser.setBoolean(3, userAuth.getEnabled());
                psUser.setBoolean(4, userAuth.getAccountNonExpired());
                psUser.setBoolean(5, userAuth.getAccountNonLocked());
                psUser.setBoolean(6, userAuth.getCredentialsNonExpired());
                psUser.executeUpdate();

                try (ResultSet resultSet = psUser.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        userAuth.setId(UUID.fromString(resultSet.getString("id")));
                    } else throw new IllegalStateException("Can't get id");

                    for (AuthorityEntity a : userAuth.getAuthorityEntities()) {
                        psAuthority.setObject(1, userAuth.getId());
                        psAuthority.setString(2, a.getAuthority().name());
                        psAuthority.addBatch();
                        psAuthority.clearParameters();
                    }
                    psAuthority.executeBatch();
                    connection.commit();
                }
            } catch (SQLException e) {
                connection.rollback();
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userAuth;
    }

    @Override
    public UserAuthEntity updateUserInAuth(UserAuthEntity userAuth) {
        try (Connection connection = userAuthDataSource.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement psUser = connection.prepareStatement(
                    "UPDATE \"user\" SET username=?, password=?, enabled=?, account_non_expired=?, account_non_locked=?, credentials_non_expired=? WHERE id=?)"
            );
                 PreparedStatement psDeleteAuthority = connection.prepareStatement(
                         "DELETE FROM \"authority\" WHERE user_id = ?"
                 );
                 PreparedStatement psAuthority = connection.prepareStatement(
                         "INSERT INTO authority (user_id, authority) VALUES(?, ?)")) {

                psUser.setString(1, userAuth.getUsername());
                psUser.setString(2, encoder.encode(userAuth.getPassword()));
                psUser.setBoolean(3, userAuth.getEnabled());
                psUser.setBoolean(4, userAuth.getAccountNonExpired());
                psUser.setBoolean(5, userAuth.getAccountNonLocked());
                psUser.setBoolean(6, userAuth.getCredentialsNonExpired());
                psUser.setObject(7, userAuth.getId());
                psUser.executeUpdate();

                psDeleteAuthority.setObject(1, userAuth.getId());
                psDeleteAuthority.executeUpdate();

                for (AuthorityEntity a : userAuth.getAuthorityEntities()) {
                    psAuthority.setObject(1, userAuth.getId());
                    psAuthority.setString(2, a.getAuthority().name());
                    psAuthority.addBatch();
                    psAuthority.clearParameters();
                }
                psAuthority.executeBatch();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new SQLException(e);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userAuth;
    }


    @Override
    public UserEntity createInUserData(UserEntity user) {
        try (Connection connection = userDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO \"user\" (username, currency, firstname, surname, photo, photo_small) VALUES(?, ?, ?, ?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setObject(2, user.getCurrency());
            preparedStatement.setString(3, user.getFirstname());
            preparedStatement.setString(4, user.getSurname());
            preparedStatement.setBytes(5, user.getPhoto());
            preparedStatement.setBytes(6, user.getPhotoSmall());
            preparedStatement.executeUpdate();

            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    user.setId(UUID.fromString(resultSet.getString("id")));
                    return user;
                } else throw new IllegalStateException("Can't get id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserEntity updateInUserData(UserEntity user) {
        try (Connection connection = userDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE \"user\" SET username=?, currency=?, firstname=?, surname=?, photo=?, photo_small=? WHERE id=?"
             )) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setObject(2, user.getCurrency());
            preparedStatement.setString(3, user.getFirstname());
            preparedStatement.setString(4, user.getUsername());
            preparedStatement.setBytes(5, user.getPhoto());
            preparedStatement.setBytes(6, user.getPhotoSmall());
            preparedStatement.setObject(7, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    @Override
    public Optional<UserEntity> findUserInUserdataById(UUID id) {
        try (Connection connection = userDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM \"user\" WHERE id = ?"
             )) {
            preparedStatement.setObject(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                UserEntity userEntity = new UserEntity();
                userEntity.setId(UUID.fromString(resultSet.getString("id")));
                userEntity.setUsername(resultSet.getString("username"));
                userEntity.setCurrency(CurrencyValues.valueOf(resultSet.getString("currency")));
                userEntity.setFirstname(resultSet.getString("firstname"));
                userEntity.setSurname(resultSet.getString("surname"));
                userEntity.setPhoto(resultSet.getBytes("photo"));
                userEntity.setPhotoSmall(resultSet.getBytes("photo_small"));
                return Optional.of(userEntity);
            } else {
                throw new RuntimeException();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserAuthEntity findUserInAuth(String username) {
        return null;
    }

    @Override
    public UserEntity findUserInUserData(String username) {
        return null;
    }
}
