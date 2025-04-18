package hei.spring.todo.dao.operations;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import hei.spring.todo.dao.CustomDataSource;
import hei.spring.todo.dao.mapper.OrderMapper;
import hei.spring.todo.model.Order;

@Repository
public class OrderCrudOperations implements CrudOperations<Order> {
	@Autowired
	private CustomDataSource customDataSource;
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private DishOrderCrudOperations dishOrderCrudOperations;

	@Override
	public List<Order> getAll(int page, int size) {
		List<Order> orders = new ArrayList<>();
		String query = "SELECT id_order, status, created_at, confirmed_at, in_preparation_at, completed_at, delivered_at, canceled_at FROM orders";
		try (Connection connection = customDataSource.getConnection();
		PreparedStatement statement = connection.prepareStatement(query)) {
			// statement.setInt(1, size);
			// statement.setInt(2, (page - 1) * size);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Order order = orderMapper.apply(resultSet);
				orders.add(order);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orders;
	}

	@Override
	public Order findById(String id) {
		Order order = new Order();
		String query = "SELECT id_order, status, created_at, confirmed_at, in_preparation_at, completed_at, delivered_at, canceled_at FROM orders WHERE id_order = ?";
		try (Connection connection = customDataSource.getConnection();
		PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, id);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				order = orderMapper.apply(resultSet);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return order;
	}

	public Order save(Order entity) {
		if (findById(entity.getIdOrder()).getIdOrder() == null) {
			String query = "INSERT INTO orders (id_order, status, created_at, confirmed_at, in_preparation_at, completed_at, delivered_at, canceled_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			try (Connection connection = customDataSource.getConnection();
		PreparedStatement statement = connection.prepareStatement(query)) {
				statement.setString(1, entity.getIdOrder());
				statement.setObject(2, entity.getStatus(), java.sql.Types.OTHER);
				statement.setTimestamp(3, Timestamp.from(entity.getCreatedAt()));
				statement.setTimestamp(4,
						entity.getConfirmedAt() != null ? Timestamp.from(entity.getConfirmedAt()) : null);
				statement.setTimestamp(5,
						entity.getInPreparationAt() != null ? Timestamp.from(entity.getInPreparationAt()) : null);
				statement.setTimestamp(6,
						entity.getCompletedAt() != null ? Timestamp.from(entity.getCompletedAt()) : null);
				statement.setTimestamp(7,
						entity.getDeliveredAt() != null ? Timestamp.from(entity.getDeliveredAt()) : null);
				statement.setTimestamp(8,
						entity.getCanceledAt() != null ? Timestamp.from(entity.getCanceledAt()) : null);
				statement.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			String query = "UPDATE orders SET status = ?, created_at = ?, confirmed_at = ?, in_preparation_at = ?, completed_at = ?, delivered_at = ?, canceled_at = ? WHERE id_order = ?";
			try (Connection connection = customDataSource.getConnection();
		PreparedStatement statement = connection.prepareStatement(query)) {
				statement.setObject(1, entity.getStatus(), java.sql.Types.OTHER);
				statement.setTimestamp(2, Timestamp.from(entity.getCreatedAt()));
				statement.setTimestamp(3,
						entity.getConfirmedAt() != null ? Timestamp.from(entity.getConfirmedAt()) : null);
				statement.setTimestamp(4,
						entity.getInPreparationAt() != null ? Timestamp.from(entity.getInPreparationAt()) : null);
				statement.setTimestamp(5,
						entity.getCompletedAt() != null ? Timestamp.from(entity.getCompletedAt()) : null);
				statement.setTimestamp(6,
						entity.getDeliveredAt() != null ? Timestamp.from(entity.getDeliveredAt()) : null);
				statement.setTimestamp(7,
						entity.getCanceledAt() != null ? Timestamp.from(entity.getCanceledAt()) : null);
				statement.setString(8, entity.getIdOrder());
				statement.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		entity.getDishOrders().forEach(dishOrder -> dishOrderCrudOperations.save(dishOrder));

		return entity;
	}

	@Override
	public List<Order> saveAll(List<Order> entities) {
		List<Order> savedOrders = new ArrayList<>();
		for (Order entity : entities) {
			savedOrders.add(save(entity));
		}
		return savedOrders;
	}


}
