package hei.spring.todo.endpoint.mapper;

import hei.spring.todo.endpoint.rest.SaleRest;
import hei.spring.todo.model.DishOrder;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Component
public class SaleRestMapper {
	public SaleRest toRest(DishOrder dishOrder) {

		return new SaleRest(dishOrder.getDish().getName(), dishOrder.getQuantity(), dishOrder.getPriceWithDateTime(dishOrder.getCompletedAt().atZone(ZoneId.systemDefault()).toLocalDate()));
	}

}
