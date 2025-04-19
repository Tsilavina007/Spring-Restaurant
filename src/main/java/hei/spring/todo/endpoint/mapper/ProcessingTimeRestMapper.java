package hei.spring.todo.endpoint.mapper;

import hei.spring.todo.endpoint.rest.ProcessingTimeRest;
import hei.spring.todo.model.DishOrder;
import org.springframework.stereotype.Component;

@Component
public class ProcessingTimeRestMapper {
	public ProcessingTimeRest toRest(DishOrder dishOrder) {
		// if (dishOrder.getTotalAmount() == null || dishOrder.getTotalAmount() == 0 ) {
		// 	return new ProcessingTimeRest(dishOrder.getDish().getIdDish(), dishOrder.getDish().getName(), dishOrder.getQuantity(), dishOrder.getPrice());
		// }
		return new ProcessingTimeRest(dishOrder.getDish().getIdDish(), dishOrder.getDish().getName(), dishOrder.getProcessingTime(), "SECONDS");
	}

}
