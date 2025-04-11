package hei.spring.todo.endpoint.mapper;

import hei.spring.todo.endpoint.rest.DishOrderRest;
import hei.spring.todo.model.DishOrder;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class DishOrderRestMapper implements Function<DishOrder, DishOrderRest> {

	@Override
	public DishOrderRest apply(DishOrder dishOrder) {
		// System.out.println(dishOrder.getPrice());
		return new DishOrderRest(
			dishOrder.getDish().getIdDish(),
			dishOrder.getDish().getName(),
			dishOrder.getDish().getUnitPrice(),
			dishOrder.getQuantity(),
			dishOrder.getActualStatus()
		);
	}

}
