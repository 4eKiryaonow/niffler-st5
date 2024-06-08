package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.DataSourceProvider;
import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.sjdbc.CategoryEntityRowMapper;
import guru.qa.niffler.data.sjdbc.SpendEntityRowMapper;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.DataBase.SPEND;

public class CategorySpendRepositoryImplSpringJdbc implements CategorySpendRepository {

    private static final JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSourceProvider.dataSource(SPEND));

    @Override
    public CategoryEntity createCategory(CategoryEntity categoryEntity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(
                            "INSERT INTO category (category, username) values (?,?)",
                            PreparedStatement.RETURN_GENERATED_KEYS
                    );
                    preparedStatement.setString(1, categoryEntity.getCategory());
                    preparedStatement.setString(2, categoryEntity.getUsername());
                    return preparedStatement;
                }, keyHolder
        );
        categoryEntity.setId((UUID) keyHolder.getKeys().get("id"));
        return categoryEntity;
    }

    @Override
    public CategoryEntity editCategory(CategoryEntity categoryEntity) {
        jdbcTemplate.update(
                "UPDATE category SET category=?, username=? WHERE id = ?",
                categoryEntity.getCategory(),
                categoryEntity.getUsername(),
                categoryEntity.getId()
        );
        return categoryEntity;
    }

    @Override
    public void removeCategory(CategoryEntity categoryEntity) {
        jdbcTemplate.update(
                "DELETE FROM category WHERE id = ?",
                categoryEntity.getId()
        );
    }

    @Override
    public SpendEntity createSpend(SpendEntity spendEntity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(
                            "INSERT INTO spend (username, spend_date, currency, amount, description, category_id) VALUES(?, ?, ?, ?, ?, ?)",
                            PreparedStatement.RETURN_GENERATED_KEYS
                    );
                    preparedStatement.setString(1, spendEntity.getUsername());
                    preparedStatement.setDate(2, new Date(spendEntity.getSpendDate().getTime()));
                    preparedStatement.setString(3, String.valueOf(spendEntity.getCurrency()));
                    preparedStatement.setDouble(4, spendEntity.getAmount());
                    preparedStatement.setString(5, spendEntity.getDescription());
                    preparedStatement.setObject(6, getCategoryByName(spendEntity.getCategory()).get().getId());
                    return preparedStatement;
                }, keyHolder
        );
        spendEntity.setId((UUID) keyHolder.getKeys().get("id"));
        return spendEntity;
    }

    @Override
    public SpendEntity editSpend(SpendEntity spendEntity) {

        jdbcTemplate.update(
                "UPDATE spend SET username=?, spend_date=?, currency=?, amount=?, description=?, category_id=? WHERE id=?",
                spendEntity.getUsername(),
                new Date(spendEntity.getSpendDate().getTime()),
                String.valueOf(spendEntity.getCurrency()),
                spendEntity.getAmount(),
                spendEntity.getDescription(),
                getCategoryByName(spendEntity.getCategory())
        );
        return spendEntity;
    }

    @Override
    public void removeSpend(SpendEntity spendEntity) {
        jdbcTemplate.update(
                "DELETE FROM category WHERE id = ?",
                spendEntity.getId()
        );
    }

    public Optional<CategoryEntity> getCategoryByName(String category) {
        try {
            return Optional.of(
                    jdbcTemplate.queryForObject(
                            "SELECT * FROM category WHERE category = ?",
                            CategoryEntityRowMapper.instance, category
                    ));
        } catch (DataRetrievalFailureException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<SpendEntity> findAllByUsername(String username) {
        return jdbcTemplate.query(
                "SELECT * FROM spend WHERE username = ?",
                SpendEntityRowMapper.instance, username
        );
    }
}