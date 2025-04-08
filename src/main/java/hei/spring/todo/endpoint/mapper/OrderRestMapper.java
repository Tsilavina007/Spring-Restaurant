package hei.spring.todo.endpoint.mapper;

import hei.spring.todo.dao.operations.DishOrderCrudOperations;
import hei.spring.todo.dao.operations.IngredientCrudOperations;
import hei.spring.todo.endpoint.rest.DishOrderRest;
import hei.spring.todo.endpoint.rest.OrderRest;
import hei.spring.todo.model.DishOrder;
import hei.spring.todo.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderRestMapper {
	@Autowired
	private DishOrderRestMapper dishOrderRestMapper;
	@Autowired
	private DishOrderCrudOperations dishOrderCrudOperations;

	public OrderRest toRest(Order order) {
		List<DishOrder> dishOrders = dishOrderCrudOperations.getAllDishOrderByidOrder(order.getIdOrder());

		List<DishOrderRest> dishOrderRests = dishOrders.stream()
				.map(dish -> dishOrderRestMapper.apply(dish)).toList();
		return new OrderRest(order.getIdOrder(), order.getActualStatus(), order.getTotalAmount(), dishOrderRests);
	}

}
