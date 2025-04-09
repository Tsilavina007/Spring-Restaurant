package hei.spring.todo.dao.operations;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import hei.spring.todo.dao.CustomDataSource;
import hei.spring.todo.dao.mapper.DishMapper;
import hei.spring.todo.model.Dish;
import hei.spring.todo.service.exception.NotFoundException;
import hei.spring.todo.service.exception.ServerException;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DishCrudOperations implements CrudOperations<Dish> {
	private final CustomDataSource customDataSource;
	private final DishMapper dishMapper;

	@Override
	public List<Dish> getAll(int page, int size) {
		List<Dish> dishes = new ArrayList<>();
		try (Connection connection = customDataSource.getConnection();
		PreparedStatement statement = connection.prepareStatement(
			"select d.id_dish, d.name from dish d order by d.id_dish asc limit ? offset ?")) {
				statement.setInt(1, size);
				statement.setInt(2, size * (page - 1));
				try (ResultSet resultSet = statement.executeQuery()) {
					while (resultSet.next()) {
					Dish dish = dishMapper.apply(resultSet);
					dishes.add(dish);
				}
				return dishes;
			}
		} catch (SQLException e) {
			throw new ServerException(e);
		}
	}

	@Override
	public Dish findById(String id) {
		try (Connection connection = customDataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(
						"select i.id_dish, i.name from dish i where i.id_dish = ?")) {
			statement.setString(1, id);
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					return dishMapper.apply(resultSet);
				}
				throw new NotFoundException("Dish.id=" + id + " not found");
			}
		} catch (SQLException e) {
			throw new ServerException(e);
		}
	}

	@Override
	public List<Dish> saveAll(List<Dish> entities) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'saveAll'");
	}



	// @Override
	// public List<Dish> saveAll(List<Dish> entities) {
	// 	List<Dish> savedDishes = new ArrayList<>();
	// 	String sqlInsertOrUpdateDish = "insert into dish (id_dish, name) values (?, ?) on conflict (id_dish) do update set name = excluded.name";
	// 	String sqlInsertOrUpdatePrice = "insert into dish_price (id_dish, unit_price, update_datetime) values (?, ?, ?)";
	// 	String sqlInsertIngredient = "insert into ingredient (id_ingredient, name) values (?, ?) on conflict (id_ingredient) do update set name = excluded.name";
	// 	String sqlInsertDishIngredient = "insert into dish_ingredient (id_dish, id_ingredient) values (?, ?)";

	// 	try (Connection connection = customDataSource.getConnection()) {
	// 		for (Dish dish : entities) {
	// 			try (PreparedStatement statement = connection.prepareStatement(sqlInsertOrUpdateDish)) {
	// 				statement.setString(1, dish.getIdDish());
	// 				statement.setString(2, dish.getName());
	// 				statement.executeUpdate();
	// 			}

	// 			for (DishPrice dishPrice : dish.getListDishPrices()) {
	// 				try (PreparedStatement priceStatement = connection.prepareStatement(sqlInsertOrUpdatePrice)) {
	// 					priceStatement.setString(1, dish.getIdDish());
	// 					priceStatement.setInt(2, dishPrice.getUnitPrice());
	// 					priceStatement.setTimestamp(3, Timestamp.valueOf(dishPrice.getUpdateDatetime()));
	// 					priceStatement.executeUpdate();
	// 				}
	// 			}

	// 			for (Ingredient ingredient : dish.getListIngredient()) {
	// 				try (PreparedStatement ingredientStatement = connection.prepareStatement(sqlInsertIngredient)) {
	// 					ingredientStatement.setString(1, ingredient.getIdIngredient());
	// 					ingredientStatement.setString(2, ingredient.getName());
	// 					ingredientStatement.executeUpdate();
	// 				}

	// 				try (PreparedStatement dishIngredientStatement = connection
	// 						.prepareStatement(sqlInsertDishIngredient)) {
	// 					dishIngredientStatement.setString(1, dish.getIdDish());
	// 					dishIngredientStatement.setString(2, ingredient.getIdIngredient());
	// 					dishIngredientStatement.executeUpdate();
	// 				}
	// 			}

	// 			savedDishes.add(dish);
	// 		}
	// 	} catch (SQLException e) {
	// 		throw new RuntimeException(e);
	// 	}

	// 	return savedDishes;
	// }

}
