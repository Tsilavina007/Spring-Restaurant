package hei.spring.todo.dao.operations;

import hei.spring.todo.dao.CustomDataSource;
import hei.spring.todo.model.DishIngredient;
import hei.spring.todo.model.Ingredient;
import hei.spring.todo.model.Unit;
import hei.spring.todo.dao.mapper.DishIngredientMapper;
import hei.spring.todo.dao.mapper.IngredientDishMapper;
import hei.spring.todo.endpoint.rest.CreateDishIngredient;
import hei.spring.todo.service.exception.ServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DishIngredientCrudOperations implements CrudOperations<DishIngredient> {
	private final CustomDataSource customDataSource;
	private final IngredientDishMapper ingredientMapper;
	private final DishIngredientMapper dishIngredientMapper;

	public List<Ingredient> findByIdDish(String id) {
		String sql = "select i.id_ingredient, i.name, di.required_quantity, di.unit from ingredient i right join dish_ingredient di on i.id_ingredient = di.id_ingredient where di.id_dish = ?";
		List<Ingredient> ingredients = new ArrayList<>();
		try (Connection connection = customDataSource.getConnection();
		PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, id);
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

	public List<Ingredient> addByIdDish(String idDish, List<CreateDishIngredient> createDishIngredients) {
		String sql = "select i.id_ingredient, i.name, di.required_quantity, di.unit from ingredient i right join dish_ingredient di on i.id_ingredient = di.id_ingredient where di.id_dish = ?";
		List<Ingredient> ingredients = new ArrayList<>();
		try (Connection connection = customDataSource.getConnection();
		PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, idDish);
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
	public List<DishIngredient> getAll(int page, int size) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getAll'");
	}

	@Override
	public DishIngredient findById(String id) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'findById'");
	}

	@Override
	public List<DishIngredient> saveAll(List<DishIngredient> entities) {
		String sql = "insert into dish_ingredient (id_dish, id_ingredient, required_quantity, unit) values (?, ?, ?, ?) returning id_dish, id_ingredient, required_quantity, unit";
		List<DishIngredient> dishIngredients = new ArrayList<>();
		try (Connection connection = customDataSource.getConnection();
					PreparedStatement statement = connection.prepareStatement(sql)) {
			entities.forEach(entityToSave -> {
				try {
					statement.setString(1, entityToSave.getIdDish());
					statement.setString(2, entityToSave.getIdIngredient());
					statement.setDouble(3, entityToSave.getRequiredQuantity());
					statement.setObject(4, (Unit) entityToSave.getUnit(), java.sql.Types.OTHER);
					statement.addBatch(); // group by batch so executed as one query in database
				} catch (SQLException e) {
					throw new ServerException(e);
				}
			});
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					dishIngredients.add(dishIngredientMapper.apply(resultSet));
				}
			}
			return dishIngredients;
		} catch (SQLException e1) {
			throw new ServerException(e1);
		}
	}

	// @Override
	// public List<DishIngredient> saveAll(List<DishIngredient> entities) {
	// 	List<DishIngredient> dishIngredients = new ArrayList<>();
	// 	try (Connection connection = customDataSource.getConnection();
	// 			PreparedStatement statement = connection.prepareStatement(
	// 					"insert into dish_ingredient (id_dish, id_ingredient, required_quantity, unit) values (?, ?, ?, ?)"
	// 							+ " returning id_dish, id_ingredient, required_quantity, unit");) {
	// 		entities.forEach(entityToSave -> {
	// 			try {
	// 				statement.setString(1, entityToSave.getDish().getIdDish());
	// 				statement.setString(2, entityToSave.getIngredient().getId());
	// 				statement.setDouble(3, entityToSave.getIngredient().getRequiredQuantity());
	// 				statement.setObject(4, (Unit) entityToSave.getIngredient().getUnit(), java.sql.Types.OTHER);
	// 				statement.addBatch(); // group by batch so executed as one query in database
	// 			} catch (SQLException e) {
	// 				throw new ServerException(e);
	// 			}
	// 		});
	// 		try (ResultSet resultSet = statement.executeQuery()) {
	// 			while (resultSet.next()) {
	// 				dishIngredients.add(dishIngredientMapper.apply(resultSet));
	// 			}
	// 		}
	// 		return dishIngredients;
	// 	}
	// }

	// @SneakyThrows
	// @Override
	// public List<Ingredient> saveAll(List<Ingredient> entities) {
	// 	List<Ingredient> ingredients = new ArrayList<>();
	// 	try (Connection connection = customDataSource.getConnection()) {
	// 		try (PreparedStatement statement = connection
	// 				.prepareStatement("insert into ingredient (id_ingredient, name) values (?, ?)"
	// 						+ " on conflict (id_ingredient) do update set name=excluded.name"
	// 						+ " returning id_ingredient, name")) {
	// 			// System.out.println(entities);
	// 			entities.forEach(entityToSave -> {
	// 				try {
	// 					statement.setString(1, entityToSave.getId());
	// 					statement.setString(2, entityToSave.getName());
	// 					statement.addBatch(); // group by batch so executed as one query in database
	// 				} catch (SQLException e) {
	// 					throw new ServerException(e);
	// 				}
	// 				if (entityToSave.getPrices() != null) {
	// 					priceCrudOperations.saveAll((entityToSave.getPrices()));
	// 				} if (entityToSave.getStockMovements() != null) {
	// 					stockMovementCrudOperations.saveAll((entityToSave.getStockMovements()));
	// 				}
	// 			});
	// 			try (ResultSet resultSet = statement.executeQuery()) {
	// 				while (resultSet.next()) {
	// 					ingredients.add(ingredientMapper.apply(resultSet));
	// 				}
	// 			}
	// 			return ingredients;
	// 		}
	// 	}
	// }
}
