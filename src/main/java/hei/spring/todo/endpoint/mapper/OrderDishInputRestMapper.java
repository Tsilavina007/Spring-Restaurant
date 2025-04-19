package hei.spring.todo.endpoint.mapper;

import hei.spring.todo.dao.operations.DishCrudOperations;
import hei.spring.todo.dao.operations.DishOrderCrudOperations;
import hei.spring.todo.endpoint.rest.OrderDishInput;
import hei.spring.todo.model.DishOrder;
import hei.spring.todo.model.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderDishInputRestMapper {
	@Autowired
	private DishOrderCrudOperations dishOrderCrudOperations;
	@Autowired
	private DishCrudOperations dishCrudOperations;

	public DishOrder toModel(String orderId, OrderDishInput orderDishInput) {
		DishOrder dishOrderToUpdate = dishOrderCrudOperations.findByIdDishAndIdOrder(orderDishInput.getDishIdentifier(), orderId);
		if (dishOrderToUpdate == null) {
			dishOrderToUpdate = dishOrderCrudOperations.save(new DishOrder(orderId, dishCrudOperations.findById(orderDishInput.getDishIdentifier()), orderDishInput.getQuantityOrdered()));
		}
		dishOrderToUpdate.setQuantity(orderDishInput.getQuantityOrdered());
		return dishOrderToUpdate;
	}

	public DishOrder updateToModel(String orderId, String dishId, Status status) {
		DishOrder dishOrderToUpdate = dishOrderCrudOperations.findByIdDishAndIdOrder(dishId, orderId);
		dishOrderToUpdate.setStatus(status);
		return dishOrderToUpdate;
	}
}
