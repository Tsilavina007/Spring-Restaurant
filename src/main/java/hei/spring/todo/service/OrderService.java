package hei.spring.todo.service;

import hei.spring.todo.dao.operations.OrderCrudOperations;
import hei.spring.todo.endpoint.mapper.OrderDishInputRestMapper;
import hei.spring.todo.endpoint.rest.OrderToUpdate;
import hei.spring.todo.model.DishOrder;
import hei.spring.todo.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
	private final OrderCrudOperations orderCrudOperations;
	private final OrderDishInputRestMapper orderDishInputRestMapper;

	public List<Order> getAll(Integer page, Integer size) {
		return orderCrudOperations.getAll(page, size);
	}

	public Order getByReference(String id) {
		return orderCrudOperations.findById(id);
	}

	public Order updateOrder(String id, OrderToUpdate orderToUpdate) {
		List<DishOrder> dishOrder = orderToUpdate.getDishes().stream()
			.map(dish -> orderDishInputRestMapper.toModel(id , dish)).toList();
		Order order = orderCrudOperations.findById(id);
		order.setListDish(dishOrder);
		return orderCrudOperations.save(order);
	}

	// public List<Ingredient> saveAll(List<Ingredient> ingredients) {
	// 	return ingredientCrudOperations.saveAll(ingredients);
	// }
}
