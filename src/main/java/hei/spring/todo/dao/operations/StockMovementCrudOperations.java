package hei.spring.todo.dao.operations;

import hei.spring.todo.dao.CustomDataSource;
import hei.spring.todo.dao.mapper.StockMovementMapper;
import hei.spring.todo.model.StockMovement;
import hei.spring.todo.model.StockMovementType;
import hei.spring.todo.model.Unit;
import hei.spring.todo.service.exception.ServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.time.Instant.now;

@Repository
public class StockMovementCrudOperations implements CrudOperations<StockMovement> {
	@Autowired
	private CustomDataSource customDataSource;
	@Autowired
	private StockMovementMapper stockMovementMapper;

	@Override
	public List<StockMovement> getAll(int page, int size) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public StockMovement findById(String id) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
	@Override
	public List<StockMovement> saveAll(List<StockMovement> entities) {
		List<StockMovement> stockMovements = new ArrayList<>();
		String sql = """
				insert into movement (id_mvm, quantity_mvm, unit, type_mvm, update_datetime, id_ingredient)
				values (?, ?, ?, ?, ?, ?)
				on conflict (id_mvm) do nothing returning id_mvm, quantity_mvm, unit, type_mvm, update_datetime, id_ingredient""";
		try (Connection connection = customDataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			for (StockMovement stockMovement : entities) {
				statement.setString(1, stockMovement.getId());
				statement.setDouble(2, stockMovement.getQuantity());
				statement.setObject(3, (Unit) stockMovement.getUnit(), java.sql.Types.OTHER);
				statement.setObject(4, (StockMovementType) stockMovement.getMovementType(), java.sql.Types.OTHER);
				statement.setTimestamp(5, Timestamp.from(now()));
				statement.setString(6, stockMovement.getIngredient().getId());
				try (ResultSet resultSet = statement.executeQuery()) {
					while (resultSet.next()) {
						stockMovements.add(stockMovementMapper.apply(resultSet));
					}
				}
			}
			return stockMovements;
		} catch (SQLException e) {
			throw new ServerException(e);
		}
	}

	public List<StockMovement> findByIdIngredient(String idIngredient) {
		List<StockMovement> stockMovements = new ArrayList<>();
		try (Connection connection = customDataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(
						"select s.id_mvm, s.quantity_mvm, s.unit, s.type_mvm, s.update_datetime from movement s"
								+ " join ingredient i on s.id_ingredient = i.id_ingredient"
								+ " where s.id_ingredient = ?")) {
			statement.setString(1, idIngredient);
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					stockMovements.add(stockMovementMapper.apply(resultSet));
				}
				return stockMovements;
			}
		} catch (SQLException e) {
			throw new ServerException(e);
		}
	}
}
