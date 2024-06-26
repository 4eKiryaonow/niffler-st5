package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.DataSourceProvider;
import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static guru.qa.niffler.data.DataBase.SPEND;

public class CategorySpendRepositoryImpl implements CategorySpendRepository {

    private static final DataSource categorySpendDataSource = DataSourceProvider.dataSource(SPEND);

    @Override
    public CategoryEntity createCategory(CategoryEntity categoryEntity) {
        try (Connection connection = categorySpendDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO category (category, username) values (?,?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, categoryEntity.getCategory());
            preparedStatement.setString(2, categoryEntity.getUsername());
            preparedStatement.executeUpdate();

            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    categoryEntity.setId(UUID.fromString(resultSet.getString("id")));
                    return categoryEntity;
                } else throw new IllegalStateException("Can't get id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CategoryEntity editCategory(CategoryEntity categoryEntity) {
        try (Connection connection = categorySpendDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE category SET category=?, username=? WHERE id = ?"
             )) {
            preparedStatement.setString(1, categoryEntity.getCategory());
            preparedStatement.setString(2, categoryEntity.getUsername());
            preparedStatement.setObject(3, categoryEntity.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categoryEntity;
    }

    @Override
    public void removeCategory(CategoryEntity categoryEntity) {
        try (Connection connection = categorySpendDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "DELETE FROM category WHERE id = ?"
             )) {
            preparedStatement.setObject(1, categoryEntity.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SpendEntity createSpend(SpendEntity spendEntity) {
        try (Connection connection = categorySpendDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO spend (username, spend_date, currency, amount, description, category_id) VALUES(?, ?, ?, ?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS
             )) {
            preparedStatement.setString(1, spendEntity.getUsername());
            preparedStatement.setDate(2, new Date(spendEntity.getSpendDate().getTime()));
            preparedStatement.setString(3, String.valueOf(spendEntity.getCurrency()));
            preparedStatement.setDouble(4, spendEntity.getAmount());
            preparedStatement.setString(5, spendEntity.getDescription());
            preparedStatement.setObject(6, getCategoryByName(spendEntity.getCategory().getCategory()).getId());
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                spendEntity.setId(UUID.fromString(resultSet.getString("id")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return spendEntity;
    }

    @Override
    public SpendEntity editSpend(SpendEntity spendEntity) {
        try (Connection connection = categorySpendDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE spend SET username=?, spend_date=?, currency=?, amount=?, description=?, category_id=? WHERE id=?"
             )) {
            preparedStatement.setString(1, spendEntity.getUsername());
            preparedStatement.setDate(2, new Date(spendEntity.getSpendDate().getTime()));
            preparedStatement.setString(3, String.valueOf(spendEntity.getCurrency()));
            preparedStatement.setDouble(4, spendEntity.getAmount());
            preparedStatement.setString(5, spendEntity.getDescription());
            preparedStatement.setObject(6, getCategoryByName(spendEntity.getCategory().getCategory()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return spendEntity;
    }

    @Override
    public void removeSpend(SpendEntity spendEntity) {
        try (Connection connection = categorySpendDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "DELETE FROM spend WHERE id = ?"
             )) {
            preparedStatement.setObject(1, spendEntity.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public CategoryEntity getCategoryByName(String category) {
        try (Connection connection = categorySpendDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM category WHERE category = ?"
             )) {
            preparedStatement.setString(1, category);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                CategoryEntity categoryEntity = new CategoryEntity();
                categoryEntity.setId(UUID.fromString(resultSet.getString("id")));
                categoryEntity.setCategory(resultSet.getString("category"));
                categoryEntity.setUsername(resultSet.getString("username"));
                return categoryEntity;
            } else {
                throw new RuntimeException();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<SpendEntity> findAllByUsername(String username) {
        List<SpendEntity> spendEntityList = new ArrayList<>();
        try (Connection connection = categorySpendDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM spend WHERE username = ?"
             )) {
            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                SpendEntity spendEntity = new SpendEntity();
                spendEntity.setId((UUID) rs.getObject("id"));
                spendEntity.setUsername(rs.getString("username"));
                spendEntity.setSpendDate(rs.getDate("spend_date"));
                spendEntity.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
                spendEntity.setAmount(rs.getDouble("amount"));
                spendEntity.setDescription(rs.getString("description"));
                spendEntity.setCategory(new CategoryEntity(UUID.fromString(rs.getString("category")), null, spendEntity.getUsername()));
                spendEntityList.add(spendEntity);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return spendEntityList;
    }

    @Override
    public CategoryEntity findCategory(String category, String username) {
        try (Connection connection = categorySpendDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM category WHERE category = ? and username = ?"
             )) {
            preparedStatement.setString(1, category);
            preparedStatement.setString(2, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                CategoryEntity categoryEntity = new CategoryEntity();
                categoryEntity.setId(UUID.fromString(resultSet.getString("id")));
                categoryEntity.setCategory(resultSet.getString("category"));
                categoryEntity.setUsername(resultSet.getString("username"));
                return categoryEntity;
            } else {
                throw new RuntimeException();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
