package hei.spring.todo.dao.operations;

import hei.spring.todo.dao.CustomDataSource;
import hei.spring.todo.dao.mapper.IngredientPriceMapper;
import hei.spring.todo.model.IngredientPrice;
import hei.spring.todo.service.exception.ServerException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class IngredientPriceCrudOperations implements CrudOperations<IngredientPrice> {
	@Autowired
	private CustomDataSource customDataSource;
	@Autowired
	private IngredientPriceMapper priceMapper;

	@Override
	public List<IngredientPrice> getAll(int page, int size) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public IngredientPrice findById(String id) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@SneakyThrows
	@Override
	public List<IngredientPrice> saveAll(List<IngredientPrice> entities) {
		List<IngredientPrice> prices = new ArrayList<>();
		try (Connection connection = customDataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(
						"insert into ingredient_price (unit_price, update_datetime, id_ingredient) values (?, ?, ?)"
								+ " returning unit_price, update_datetime, id_ingredient");) {
			entities.forEach(entityToSave -> {
				try {
					statement.setDouble(1, entityToSave.getAmount());
					statement.setDate(2, Date.valueOf(entityToSave.getDateValue()));
					statement.setString(3, entityToSave.getIngredient().getId());
					statement.addBatch(); // group by batch so executed as one query in database
				} catch (SQLException e) {
					throw new ServerException(e);
				}
			});
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					prices.add(priceMapper.apply(resultSet));
				}
			}
			return prices;
		}
	}

	public List<IngredientPrice> findByIdIngredient(String idIngredient) {
		List<IngredientPrice> prices = new ArrayList<>();
		try (Connection connection = customDataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(
						"select p.id_ingredient, p.unit_price, p.update_datetime from ingredient_price p"
								+ " join ingredient i on p.id_ingredient = i.id_ingredient"
								+ " where p.id_ingredient = ?")) {
			statement.setString(1, idIngredient);
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					IngredientPrice price = priceMapper.apply(resultSet);
					prices.add(price);
				}
				return prices;
			}
		} catch (SQLException e) {
			throw new ServerException(e);
		}
	}
}
