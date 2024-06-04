package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.DataBaseConnection;
import guru.qa.niffler.data.DataSourceProvider;
import guru.qa.niffler.data.constants.query.QueryTableCategory;
import guru.qa.niffler.data.constants.query.QueryTableSpend;
import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static guru.qa.niffler.data.DataBase.SPEND;
import static guru.qa.niffler.data.constants.TableNames.CATEGORY_TABLE;
import static guru.qa.niffler.data.constants.TableNames.SPEND_TABLE;
import static guru.qa.niffler.data.constants.query.CommonQuery.DELETE;
import static guru.qa.niffler.data.constants.query.QueryTableCategory.INSERT;
import static guru.qa.niffler.data.constants.query.QueryTableCategory.UPDATE;

public class CategorySpendRepositoryImpl implements CategorySpendRepository {

    private static final DataSource categorySpendDataSource = DataSourceProvider.dataSource(SPEND);

    @Override
    public CategoryEntity createCategory(CategoryEntity categoryEntity) {
        String query = String.format(INSERT, CATEGORY_TABLE);
        PreparedStatement preparedStatement = DataBaseConnection.prepareStatement(categorySpendDataSource, query);
        try {
            preparedStatement.setString(1, categoryEntity.getCategory());
            preparedStatement.setString(2, categoryEntity.getUsername());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                categoryEntity.setId(UUID.fromString(resultSet.getString("id")));
                return categoryEntity;
            } else throw new RuntimeException();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DataBaseConnection.closeStatement(preparedStatement);
        }
    }

    @Override
    public CategoryEntity editCategory(CategoryEntity categoryEntity) {
        String query = String.format(UPDATE, CATEGORY_TABLE);
        PreparedStatement preparedStatement = DataBaseConnection.prepareStatement(categorySpendDataSource, query);
        try {
            preparedStatement.setString(1, categoryEntity.getCategory());
            preparedStatement.setString(2, categoryEntity.getUsername());
            preparedStatement.setObject(3, categoryEntity.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DataBaseConnection.closeStatement(preparedStatement);
        }
        return categoryEntity;
    }

    @Override
    public void removeCategory(CategoryEntity categoryEntity) {
        String query = String.format(DELETE, CATEGORY_TABLE);
        PreparedStatement preparedStatement = DataBaseConnection.prepareStatement(categorySpendDataSource, query);
        try {
            preparedStatement.setObject(1, categoryEntity.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DataBaseConnection.closeStatement(preparedStatement);
        }
    }

    @Override
    public SpendEntity createSpend(SpendEntity spendEntity) {
        String query = String.format(QueryTableSpend.INSERT, SPEND_TABLE);
        PreparedStatement preparedStatement = DataBaseConnection.prepareStatement(categorySpendDataSource, query);
        try {
            preparedStatement.setString(1, spendEntity.getUsername());
            preparedStatement.setDate(2, new Date(spendEntity.getSpendDate().getTime()));
            preparedStatement.setString(3, String.valueOf(spendEntity.getCurrency()));
            preparedStatement.setDouble(4, spendEntity.getAmount());
            preparedStatement.setString(5, spendEntity.getDescription());
            preparedStatement.setObject(6, spendEntity.getCategory());
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                spendEntity.setId(UUID.fromString(resultSet.getString("id")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DataBaseConnection.closeStatement(preparedStatement);
        }
        return spendEntity;
    }

    @Override
    public SpendEntity editSpend(SpendEntity spendEntity) {
        String query = String.format(QueryTableSpend.UPDATE, SPEND_TABLE);
        PreparedStatement preparedStatement = DataBaseConnection.prepareStatement(categorySpendDataSource, query);
        try {
            preparedStatement.setString(1, spendEntity.getUsername());
            preparedStatement.setDate(2, new Date(spendEntity.getSpendDate().getTime()));
            preparedStatement.setString(3, String.valueOf(spendEntity.getCurrency()));
            preparedStatement.setDouble(4, spendEntity.getAmount());
            preparedStatement.setString(5, spendEntity.getDescription());
            preparedStatement.setObject(6, getCategoryByName(spendEntity.getCategory()).getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return spendEntity;
    }

    @Override
    public void removeSpend(SpendEntity spendEntity) {
        String query = String.format(DELETE, SPEND_TABLE);
        PreparedStatement preparedStatement = DataBaseConnection.prepareStatement(categorySpendDataSource, query);
        try {
            preparedStatement.setObject(1, spendEntity.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public CategoryEntity getCategoryByName(String category) {
        String query = String.format(QueryTableCategory.SELECT_BY_NAME, CATEGORY_TABLE);
        PreparedStatement preparedStatement = DataBaseConnection.prepareStatement(categorySpendDataSource, query);
        try {
            preparedStatement.setString(1, category);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                CategoryEntity categoryEntity = new CategoryEntity();
                categoryEntity.setId(UUID.fromString(resultSet.getString("id")));
                categoryEntity.setCategory(resultSet.getString("category"));
                categoryEntity.setUsername(resultSet.getString("username"));
                return new CategoryEntity(
                );
            } else {
                throw new RuntimeException();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
