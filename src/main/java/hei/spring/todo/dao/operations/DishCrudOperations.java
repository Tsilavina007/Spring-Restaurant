package hei.spring.todo.dao.operations;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import hei.spring.todo.dao.CustomDataSource;
import hei.spring.todo.dao.mapper.DishMapper;
import hei.spring.todo.model.Dish;
import hei.spring.todo.model.Ingredient;
import hei.spring.todo.service.exception.NotFoundException;
import hei.spring.todo.service.exception.ServerException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Repository
public class DishCrudOperations implements CrudOperations<Dish> {
	@Autowired
	private CustomDataSource customDataSource;
	@Autowired
	private DishMapper dishMapper;
	@Autowired
	private DishPriceCrudOperations priceCrudOperations;
	@Autowired
	private IngredientCrudOperations ingredientCrudOperations;
	@Autowired
	private DishIngredientCrudOperations dishIngredientCrudOperations;

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



	@SneakyThrows
	public List<Dish> saveAll2(List<Dish> entities) {
		List<Dish> dishes = new ArrayList<>();
				try (Connection connection = customDataSource.getConnection()) {
					try (PreparedStatement statement = connection
							.prepareStatement("insert into dish (id_dish, name) values (?, ?)"
									+ " on conflict (id_dih) do update set name=excluded.name"
									+ " returning id_dish, name")) {
						// System.out.println(entities);
						entities.forEach(entityToSave -> {
							try {
								statement.setString(1, entityToSave.getIdDish());
								statement.setString(2, entityToSave.getName());
								statement.addBatch(); // group by batch so executed as one query in database
							} catch (SQLException e) {
								throw new ServerException(e);
							}
							if (entityToSave.getDishPrices() != null) {
								priceCrudOperations.saveAll((entityToSave.getDishPrices()));
							}
							if (entityToSave.getIngredients() != null) {
								ingredientCrudOperations.saveAll((entityToSave.getIngredients()));
							}
						});
						try (ResultSet resultSet = statement.executeQuery()) {
							while (resultSet.next()) {
								dishes.add(dishMapper.apply(resultSet));
							}
						}
						return dishes;
					}
				}
			}
	}
