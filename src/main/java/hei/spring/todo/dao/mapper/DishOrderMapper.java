package hei.spring.todo.dao.mapper;

import hei.spring.todo.model.Dish;
import hei.spring.todo.model.DishOrder;
import hei.spring.todo.model.Status;
import hei.spring.todo.dao.operations.DishCrudOperations;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class DishOrderMapper implements Function<ResultSet, DishOrder> {
	private final DishCrudOperations dishCrudOperations;

	@SneakyThrows
	@Override
	public DishOrder apply(ResultSet resultSet) {
		String idDish = resultSet.getString("id_dish");
		Dish dish = dishCrudOperations.findById(idDish);

		DishOrder applyDish = new DishOrder();
		applyDish.setIdOrder(resultSet.getString("id_order"));
		applyDish.setDish(dish);
		applyDish.addQuantity(resultSet.getInt("quantity"));
		applyDish.setStatus(Status.valueOf(resultSet.getString("status")));
		applyDish.setCreatedAt(resultSet.getTimestamp("created_at") != null ? resultSet.getTimestamp("created_at").toInstant() : null);
		applyDish.setCreatedAt(resultSet.getTimestamp("confirmed_at") != null ? resultSet.getTimestamp("confirmed_at").toInstant() : null);
		applyDish.setCreatedAt(resultSet.getTimestamp("in_preparation_at") != null ? resultSet.getTimestamp("in_preparation_at").toInstant() : null);
		applyDish.setCreatedAt(resultSet.getTimestamp("completed_at") != null ? resultSet.getTimestamp("completed_at").toInstant() : null);
		applyDish.setCreatedAt(resultSet.getTimestamp("delivered_at") != null ? resultSet.getTimestamp("delivered_at").toInstant() : null);
		applyDish.setCreatedAt(resultSet.getTimestamp("canceled_at") != null ? resultSet.getTimestamp("canceled_at").toInstant() : null);

		return applyDish;
	}
}
