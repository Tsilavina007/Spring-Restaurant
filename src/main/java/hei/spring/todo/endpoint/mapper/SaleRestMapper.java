package hei.spring.todo.endpoint.mapper;

import hei.spring.todo.endpoint.rest.SaleRest;
import hei.spring.todo.model.DishOrder;
import org.springframework.stereotype.Component;

@Component
public class SaleRestMapper {
	public SaleRest toRest(DishOrder dishOrder) {
		if (dishOrder.getTotalAmount() == null || dishOrder.getTotalAmount() == 0 ) {
			return new SaleRest(dishOrder.getDish().getIdDish(), dishOrder.getDish().getName(), dishOrder.getQuantity(), dishOrder.getPrice());
		}
		return new SaleRest(dishOrder.getDish().getIdDish(), dishOrder.getDish().getName(), dishOrder.getQuantity(), dishOrder.getTotalAmount());
	}

}
