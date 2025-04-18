package hei.spring.todo.dao.operations;

import hei.spring.todo.dao.CustomDataSource;
import hei.spring.todo.dao.mapper.DishOrderMapper;
import hei.spring.todo.model.DishOrder;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DishOrderCrudOperations implements CrudOperations<DishOrder> {
	@Autowired
	private CustomDataSource customDataSource;
	@Autowired
	private DishOrderMapper dishOrderMapper;

	public List<DishOrder> getAllDishOrderByidOrder(String idOrder) {
		List<DishOrder> dishes = new ArrayList<>();
		String query = "SELECT id_dish, id_order, status, quantity, created_at, confirmed_at, in_preparation_at, completed_at, delivered_at, canceled_at FROM dish_order WHERE id_order = ?";
		try (Connection connection = customDataSource.getConnection();
		PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, idOrder);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				DishOrder dish = dishOrderMapper.apply(resultSet);
				// System.out.println(dish.getCreatedAt());
				dishes.add(dish);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dishes;
	}
	public List<DishOrder> getBestSales(LocalDate startDate, LocalDate endDate) {
		List<DishOrder> dishes = new ArrayList<>();
		String query = "SELECT id_dish, id_order, status, quantity, created_at, confirmed_at, in_preparation_at, completed_at, delivered_at, canceled_at FROM dish_order WHERE completed_at >= ? AND completed_at < ? ";
		try (Connection connection = customDataSource.getConnection();
		PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setTimestamp(1, Timestamp.valueOf(startDate.atStartOfDay()));
			statement.setTimestamp(2, Timestamp.valueOf(endDate.plusDays(1).atStartOfDay()));
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				DishOrder dish = dishOrderMapper.apply(resultSet);
				dishes.add(dish);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dishes;
	}
	@Override
	public List<DishOrder> getAll(int page, int size) {
		List<DishOrder> dishOrders = new ArrayList<>();
		String query = "SELECT id_order, id_dish, status, created_at, confirmed_at, in_preparation_at, completed_at, delivered_at, canceled_at FROM dish_order LIMIT ? OFFSET ?";
		try (Connection connection = customDataSource.getConnection();
		PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, size);
			statement.setInt(2, (page - 1) * size);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				DishOrder order = dishOrderMapper.apply(resultSet);
				dishOrders.add(order);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dishOrders;
	}

	public DishOrder findByIdDishAndIdOrder(String idDish, String idOrder) {
		DishOrder dishOrder = new DishOrder();
		String query = "SELECT id_order, id_dish, quantity, status, created_at, confirmed_at, in_preparation_at, completed_at, delivered_at, canceled_at FROM dish_order WHERE id_order = ? AND id_dish = ?";
		try (Connection connection = customDataSource.getConnection();
		PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, idOrder);
			statement.setString(2, idDish);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				dishOrder = dishOrderMapper.apply(resultSet);
			} else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dishOrder;
	}

	@Override
	public DishOrder findById(String id) {
		DishOrder dishOrder = new DishOrder();
		String query = "SELECT id_order, id_dish, status, created_at, confirmed_at, in_preparation_at, completed_at, delivered_at, canceled_at FROM dish_order WHERE id_order = ?";
		try (Connection connection = customDataSource.getConnection();
		PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, id);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				dishOrder = dishOrderMapper.apply(resultSet);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dishOrder;
	}

	public DishOrder save(DishOrder entity) {
		if (findByIdDishAndIdOrder(entity.getDish().getIdDish(), entity.getIdOrder()) == null) {
			String query = "INSERT INTO dish_order (id_order, id_dish, quantity, status, created_at, confirmed_at, in_preparation_at, completed_at, delivered_at, canceled_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			try (Connection connection = customDataSource.getConnection();
			PreparedStatement statement = connection.prepareStatement(query)) {
				statement.setString(1, entity.getIdOrder());
				statement.setString(2, entity.getDish().getIdDish());
				statement.setDouble(3, entity.getQuantity());
				statement.setObject(4, entity.getStatus(), java.sql.Types.OTHER);
				statement.setTimestamp(5, Timestamp.from(entity.getCreatedAt()));
				statement.setTimestamp(6, entity.getConfirmedAt() != null ? Timestamp.from(entity.getConfirmedAt()) : null);
				statement.setTimestamp(7, entity.getInPreparationAt() != null ? Timestamp.from(entity.getInPreparationAt()) : null);
				statement.setTimestamp(8, entity.getCompletedAt() != null ? Timestamp.from(entity.getCompletedAt()) : null);
				statement.setTimestamp(9, entity.getDeliveredAt() != null ? Timestamp.from(entity.getDeliveredAt()) : null);
				statement.setTimestamp(10, entity.getCanceledAt() != null ? Timestamp.from(entity.getCanceledAt()) : null);
				statement.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			String query = "UPDATE dish_order SET status = ?, created_at = ?, confirmed_at = ?, in_preparation_at = ?, completed_at = ?, delivered_at = ?, canceled_at = ?, quantity = ? WHERE id_order = ? AND id_dish = ?";
			try (Connection connection = customDataSource.getConnection();
			PreparedStatement statement = connection.prepareStatement(query)) {
				statement.setObject(1,entity.getStatus(), java.sql.Types.OTHER);
				statement.setTimestamp(2, Timestamp.from(entity.getCreatedAt()));
				statement.setTimestamp(3, entity.getConfirmedAt() != null ? Timestamp.from(entity.getConfirmedAt()) : null);
				statement.setTimestamp(4, entity.getInPreparationAt() != null ? Timestamp.from(entity.getInPreparationAt()) : null);
				statement.setTimestamp(5, entity.getCompletedAt() != null ? Timestamp.from(entity.getCompletedAt()) : null);
				statement.setTimestamp(6, entity.getDeliveredAt() != null ? Timestamp.from(entity.getDeliveredAt()) : null);
				statement.setTimestamp(7, entity.getCanceledAt() != null ? Timestamp.from(entity.getCanceledAt()) : null);
				statement.setDouble(8, entity.getQuantity());
				statement.setString(9, entity.getIdOrder());
				statement.setString(10, entity.getDish().getIdDish());
				statement.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
		return entity;
	}


	@Override
	public List<DishOrder> saveAll(List<DishOrder> entities) {
		List<DishOrder> savedDishOrders = new ArrayList<>();
		for (DishOrder entity : entities) {
			savedDishOrders.add(save(entity));
		}
		return savedDishOrders;
	}
}
