package hei.spring.todo.dao.operations;

import hei.spring.todo.dao.CustomDataSource;
import hei.spring.todo.model.Ingredient;
import hei.spring.todo.dao.mapper.IngredientDishMapper;
import hei.spring.todo.dao.mapper.IngredientMapper;
import hei.spring.todo.service.exception.NotFoundException;
import hei.spring.todo.service.exception.ServerException;
import lombok.SneakyThrows;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class IngredientCrudOperations implements CrudOperations<Ingredient> {
	@Autowired
	private CustomDataSource customDataSource;
	@Autowired
	private IngredientMapper ingredientMapper;
	@Autowired
	private IngredientDishMapper ingredientDishMapper;
	@Autowired
	private IngredientPriceCrudOperations priceCrudOperations;
	@Autowired
	private StockMovementCrudOperations stockMovementCrudOperations;

	// TODO : default values for page and size
	@Override
	public List<Ingredient> getAll(int page, int size) {
		List<Ingredient> ingredients = new ArrayList<>();
		try (Connection connection = customDataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(
						"select i.id_ingredient, i.name from ingredient i order by i.id_ingredient asc limit ? offset ?")) {
			statement.setInt(1, size);
			statement.setInt(2, size * (page - 1));
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					Ingredient ingredient = ingredientMapper.apply(resultSet);
					ingredients.add(ingredient);
				}
				return ingredients;
			}
		} catch (SQLException e) {
			throw new ServerException(e);
		}
	}

	@Override
	public Ingredient findById(String id) {
		try (Connection connection = customDataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(
						"select i.id_ingredient, i.name from ingredient i where i.id_ingredient = ?")) {
			statement.setString(1, id);
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					return ingredientMapper.apply(resultSet);
				}
				throw new NotFoundException("Ingredient.id=" + id + " not found");
			}
		} catch (SQLException e) {
			throw new ServerException(e);
		}
	}
	public List<Ingredient> findByIdDish(String id) {
		String sql = "select i.id_ingredient, i.name, di.required_quantity, di.unit from ingredient i right join dish_ingredient di on i.id_ingredient = di.id_ingredient where di.id_dish = ?";
		List<Ingredient> ingredients = new ArrayList<>();
		try (Connection connection = customDataSource.getConnection();
		PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, id);
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					Ingredient ingredient = ingredientDishMapper.apply(resultSet);
					ingredients.add(ingredient);
				}
				return ingredients;
			}
		} catch (SQLException e) {
			throw new ServerException(e);
		}
	}

	@SneakyThrows
	@Override
	public List<Ingredient> saveAll(List<Ingredient> entities) {
		List<Ingredient> ingredients = new ArrayList<>();
		try (Connection connection = customDataSource.getConnection()) {
			try (PreparedStatement statement = connection
					.prepareStatement("insert into ingredient (id_ingredient, name) values (?, ?)"
							+ " on conflict (id_ingredient) do update set name=excluded.name"
							+ " returning id_ingredient, name")) {
				// System.out.println(entities);
				entities.forEach(entityToSave -> {
					try {
						statement.setString(1, entityToSave.getId());
						statement.setString(2, entityToSave.getName());
						statement.addBatch(); // group by batch so executed as one query in database
					} catch (SQLException e) {
						throw new ServerException(e);
					}
					if (entityToSave.getPrices() != null) {
						priceCrudOperations.saveAll((entityToSave.getPrices()));
					} if (entityToSave.getStockMovements() != null) {
						stockMovementCrudOperations.saveAll((entityToSave.getStockMovements()));
					}
				});
				try (ResultSet resultSet = statement.executeQuery()) {
					while (resultSet.next()) {
						ingredients.add(ingredientMapper.apply(resultSet));
					}
				}
				return ingredients;
			}
		}
	}
}
