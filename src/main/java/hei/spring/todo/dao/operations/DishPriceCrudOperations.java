package hei.spring.todo.dao.operations;

import hei.spring.todo.dao.CustomDataSource;
import hei.spring.todo.dao.mapper.DishPriceMapper;
import hei.spring.todo.model.price.DishPrice;
import hei.spring.todo.service.exception.ServerException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DishPriceCrudOperations implements CrudOperations<DishPrice> {
	@Autowired
	private CustomDataSource customDataSource;
	@Autowired
	private DishPriceMapper priceMapper;

	@Override
	public List<DishPrice> getAll(int page, int size) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public DishPrice findById(String id) {
		throw new UnsupportedOperationException("Not supported yet.");
	}


	@SneakyThrows
	public DishPrice save(DishPrice dishPrice) {
		DishPrice newDishPrice = new DishPrice();
		try (Connection connection = customDataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(
						"insert into dish_price (unit_price, update_datetime, id_dish) values (?, ?, ?)"
								+ " returning unit_price, update_datetime, id_dish")) {
			statement.setDouble(1, dishPrice.getUnitPrice());
			statement.setDate(2, Date.valueOf(dishPrice.getUpdateDatetime()));
			statement.setString(3, dishPrice.getIdDish());
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					newDishPrice = priceMapper.apply(resultSet);
					newDishPrice.setIdDish(dishPrice.getIdDish());
				}
			}
		}
		return newDishPrice;
	}
	@SneakyThrows
	@Override
	public List<DishPrice> saveAll(List<DishPrice> entities) {
		List<DishPrice> prices = new ArrayList<>();
		entities.forEach(entityToSave -> {
			prices.add(save(entityToSave));
		});
		return prices;
	}

	public List<DishPrice> findByIdDish(String idDish) {
		List<DishPrice> prices = new ArrayList<>();
		try (Connection connection = customDataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(
						"select p.id_dish, p.unit_price, p.update_datetime from dish_price p"
								+ " join dish d on p.id_dish = d.id_dish"
								+ " where p.id_dish = ?")) {
			statement.setString(1, idDish);
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					DishPrice price = priceMapper.apply(resultSet);
					price.setIdDish(idDish);
					prices.add(price);
				}
				return prices;
			}
		} catch (SQLException e) {
			throw new ServerException(e);
		}
	}
}
