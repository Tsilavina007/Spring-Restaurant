package hei.spring.todo.endpoint.mapper;

import hei.spring.todo.dao.operations.DishOrderCrudOperations;
import hei.spring.todo.endpoint.rest.OrderDishInput;
import hei.spring.todo.model.DishOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderDishInputRestMapper {
	@Autowired
	private DishOrderCrudOperations dishOrderCrudOperations;

	public DishOrder toModel(String orderId, OrderDishInput orderDishInput) {
		DishOrder dishOrderToUpdate = dishOrderCrudOperations.findByIdDishAndIdOrder(orderDishInput.getIdDish(), orderId);
		System.out.println(dishOrderToUpdate);
		dishOrderToUpdate.setQuantity(orderDishInput.getQuantity());
		return dishOrderToUpdate;
	}
}
