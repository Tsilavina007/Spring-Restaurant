package hei.spring.todo.dao.mapper;

import hei.spring.todo.model.DishOrder;
import hei.spring.todo.model.Order;
import hei.spring.todo.model.Status;
import hei.spring.todo.dao.operations.DishOrderCrudOperations;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class OrderMapper implements Function<ResultSet, Order> {
	private final DishOrderCrudOperations dishCrudOperations;

	@SneakyThrows
	@Override
	public Order apply(ResultSet resultSet) {
		String idOrder = resultSet.getString("id_order");
		List<DishOrder> dishesOrder = dishCrudOperations.getAllDishOrderByidOrder(idOrder);

		Order applyDish = new Order();
		applyDish.setIdOrder(resultSet.getString("id_order"));
		applyDish.setStatus(Status.valueOf(resultSet.getString("status")));
		applyDish.setCreatedAt(resultSet.getTimestamp("created_at") != null ? resultSet.getTimestamp("created_at").toInstant() : null);
		applyDish.setConfirmedAt(resultSet.getTimestamp("confirmed_at") != null ? resultSet.getTimestamp("confirmed_at").toInstant() : null);
		applyDish.setInPreparationAt(resultSet.getTimestamp("in_preparation_at") != null ? resultSet.getTimestamp("in_preparation_at").toInstant() : null);
		applyDish.setCompletedAt(resultSet.getTimestamp("completed_at") != null ? resultSet.getTimestamp("completed_at").toInstant() : null);
		applyDish.setDeliveredAt(resultSet.getTimestamp("delivered_at") != null ? resultSet.getTimestamp("delivered_at").toInstant() : null);
		applyDish.setCanceledAt(resultSet.getTimestamp("canceled_at") != null ? resultSet.getTimestamp("canceled_at").toInstant() : null);

		applyDish.setListDish(dishesOrder);

		return applyDish;
	}
}
