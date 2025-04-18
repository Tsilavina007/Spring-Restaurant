package hei.spring.todo.endpoint.mapper;

import hei.spring.todo.dao.operations.DishOrderCrudOperations;
import hei.spring.todo.endpoint.rest.DishOrderRest;
import hei.spring.todo.endpoint.rest.OrderRest;
import hei.spring.todo.endpoint.rest.SaleRest;
import hei.spring.todo.model.DishOrder;
import hei.spring.todo.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SaleRestMapper {
	public SaleRest toRest(DishOrder dishOrder) {
		return new SaleRest(dishOrder.getDish().getName(), dishOrder.getQuantity(), dishOrder.getPrice());
	}

}
