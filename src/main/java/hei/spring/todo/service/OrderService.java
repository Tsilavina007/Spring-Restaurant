package hei.spring.todo.service;

import hei.spring.todo.dao.operations.OrderCrudOperations;
import hei.spring.todo.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
	private final OrderCrudOperations orderCrudOperations;

	public List<Order> getAll(Integer page, Integer size) {
		return orderCrudOperations.getAll(page, size);
	}

	public Order getByReference(String id) {
		return orderCrudOperations.findById(id);
	}

	// public List<Ingredient> saveAll(List<Ingredient> ingredients) {
	// 	return ingredientCrudOperations.saveAll(ingredients);
	// }
}
