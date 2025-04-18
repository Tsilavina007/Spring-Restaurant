package hei.spring.todo.endpoint.mapper;

import hei.spring.todo.dao.operations.DishCrudOperations;
import hei.spring.todo.dao.operations.DishOrderCrudOperations;
import hei.spring.todo.endpoint.rest.OrderDishInput;
import hei.spring.todo.model.DishOrder;
import hei.spring.todo.model.Status;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderDishInputRestMapper {
	@Autowired
	private DishOrderCrudOperations dishOrderCrudOperations;
	@Autowired
	private DishCrudOperations dishCrudOperations;

	public DishOrder toModel(String orderId, OrderDishInput orderDishInput) {
		// System.out.println(orderDishInput.getQuantity());
		DishOrder dishOrderToUpdate = dishOrderCrudOperations.findByIdDishAndIdOrder(orderDishInput.getIdDish(), orderId);
		if (dishOrderToUpdate == null) {
			System.out.println(orderDishInput.getIdDish());
			dishOrderToUpdate = dishOrderCrudOperations.save(new DishOrder(orderId, dishCrudOperations.findById(orderDishInput.getIdDish()), orderDishInput.getQuantity()));
		}
		dishOrderToUpdate.setQuantity(orderDishInput.getQuantity());
		return dishOrderToUpdate;
	}

	public DishOrder updateToModel(String orderId, String dishId, Status status) {
		DishOrder dishOrderToUpdate = dishOrderCrudOperations.findByIdDishAndIdOrder(dishId, orderId);
		// System.out.println(dishOrderToUpdate.getStatus());
		dishOrderToUpdate.setStatus(status);
		return dishOrderToUpdate;
	}
}
