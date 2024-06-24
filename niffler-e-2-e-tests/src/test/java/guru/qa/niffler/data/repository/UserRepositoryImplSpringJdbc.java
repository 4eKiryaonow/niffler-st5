package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.DataSourceProvider;
import guru.qa.niffler.data.entity.UserAuthEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.sjdbc.UserEntityRowMapper;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.DataBase.AUTH;
import static guru.qa.niffler.data.DataBase.USER_DATA;

public class UserRepositoryImplSpringJdbc implements UserRepository {


    private static final PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    private static final JdbcTemplate authJdbcTemplate = new JdbcTemplate(DataSourceProvider.dataSource(AUTH));
    private static final JdbcTemplate userDataJdbcTemplate = new JdbcTemplate(DataSourceProvider.dataSource(USER_DATA));
    private static final TransactionTemplate authTxTemplate = new TransactionTemplate(
            new JdbcTransactionManager(DataSourceProvider.dataSource(AUTH)));

    @Override
    public UserAuthEntity createUserInAuth(UserAuthEntity userAuth) {
        return authTxTemplate.execute(status -> {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            authJdbcTemplate.update(con -> {
                        PreparedStatement preparedStatement = con.prepareStatement(
                                "INSERT INTO \"user\" (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) VALUES(?, ?, ?, ?, ?, ?)",
                                PreparedStatement.RETURN_GENERATED_KEYS
                        );
                        preparedStatement.setString(1, userAuth.getUsername());
                        preparedStatement.setString(2, encoder.encode(userAuth.getPassword()));
                        preparedStatement.setBoolean(3, userAuth.getEnabled());
                        preparedStatement.setBoolean(4, userAuth.getAccountNonExpired());
                        preparedStatement.setBoolean(5, userAuth.getAccountNonLocked());
                        preparedStatement.setBoolean(6, userAuth.getCredentialsNonExpired());
                        return preparedStatement;
                    }, keyHolder
            );
            userAuth.setId((UUID) keyHolder.getKeys().get("id"));

            authJdbcTemplate.batchUpdate("INSERT INTO authority (user_id, authority) VALUES(?, ?)",
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setObject(1, userAuth.getId());
                            ps.setString(2, userAuth.getAuthorities().get(i).getAuthority().name());
                        }

                        @Override
                        public int getBatchSize() {
                            return userAuth.getAuthorities().size();
                        }
                    });
            return userAuth;
        });
    }

    @Override
    public UserAuthEntity updateUserInAuth(UserAuthEntity userAuth) {
        return authTxTemplate.execute(status -> {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            authJdbcTemplate.update(con -> {
                        PreparedStatement preparedStatement = con.prepareStatement(
                                "UPDATE \"user\" SET username=?, password=?, enabled=?, account_non_expired=?, account_non_locked=?, credentials_non_expired=? WHERE id=?",
                                PreparedStatement.RETURN_GENERATED_KEYS
                        );
                        preparedStatement.setString(1, userAuth.getUsername());
                        preparedStatement.setString(2, encoder.encode(userAuth.getPassword()));
                        preparedStatement.setBoolean(3, userAuth.getEnabled());
                        preparedStatement.setBoolean(4, userAuth.getAccountNonExpired());
                        preparedStatement.setBoolean(5, userAuth.getAccountNonLocked());
                        preparedStatement.setBoolean(6, userAuth.getCredentialsNonExpired());
                        preparedStatement.setObject(7, userAuth.getId());
                        return preparedStatement;
                    }, keyHolder
            );
            authJdbcTemplate.update("DELETE FROM \"authority\" WHERE user_id = ?", userAuth.getId());

            authJdbcTemplate.batchUpdate("INSERT INTO authority (user_id, authority) VALUES(?, ?)",
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setObject(1, userAuth.getId());
                            ps.setString(2, userAuth.getAuthorities().get(i).getAuthority().name());
                        }

                        @Override
                        public int getBatchSize() {
                            return userAuth.getAuthorities().size();
                        }
                    });
            return userAuth;
        });
    }


    @Override
    public UserEntity createInUserData(UserEntity user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        userDataJdbcTemplate.update(con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(
                            "INSERT INTO \"user\" (username, currency, firstname, surname, photo, photo_small) VALUES(?, ?, ?, ?, ?, ?)",
                            PreparedStatement.RETURN_GENERATED_KEYS
                    );
                    preparedStatement.setString(1, user.getUsername());
                    preparedStatement.setObject(2, user.getCurrency().name());
                    preparedStatement.setString(3, user.getFirstname());
                    preparedStatement.setString(4, user.getSurname());
                    preparedStatement.setBytes(5, user.getPhoto());
                    preparedStatement.setBytes(6, user.getPhotoSmall());
                    return preparedStatement;
                }, keyHolder
        );
        user.setId((UUID) keyHolder.getKeys().get("id"));
        return user;
    }

    @Override
    public UserEntity updateInUserData(UserEntity user) {
        userDataJdbcTemplate.update(
                "UPDATE \"user\" SET username=?, currency=?, firstname=?, surname=?, photo=?, photo_small=? WHERE id=?",
                user.getUsername(),
                user.getCurrency().name(),
                user.getFirstname(),
                user.getSurname(),
                user.getPhoto(),
                user.getPhotoSmall(),
                user.getId()
        );
        return user;
    }

    @Override
    public Optional<UserEntity> findUserInUserdataById(UUID id) {
        try {
            return Optional.of(
                    userDataJdbcTemplate.queryForObject(
                            "SELECT * FROM \"user\" WHERE id = ?",
                            UserEntityRowMapper.instance, id
                    ));
        } catch (DataRetrievalFailureException e) {
            return Optional.empty();
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
