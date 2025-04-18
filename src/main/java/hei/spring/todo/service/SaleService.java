package hei.spring.todo.service;

import hei.spring.todo.dao.operations.DishOrderCrudOperations;
import hei.spring.todo.dao.operations.OrderCrudOperations;
import hei.spring.todo.dao.operations.StockMovementCrudOperations;
import hei.spring.todo.endpoint.mapper.OrderDishInputRestMapper;
import hei.spring.todo.endpoint.mapper.SaleRestMapper;
import hei.spring.todo.endpoint.rest.DishOrderToUpdate;
import hei.spring.todo.endpoint.rest.OrderToUpdate;
import hei.spring.todo.endpoint.rest.SaleRest;
import hei.spring.todo.model.Dish;
import hei.spring.todo.model.DishOrder;
import hei.spring.todo.model.Order;
import hei.spring.todo.model.Status;
import hei.spring.todo.model.StockMovement;
import hei.spring.todo.model.StockMovementType;
import hei.spring.todo.service.exception.ClientException;
import hei.spring.todo.service.exception.ServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SaleService {
	private final DishOrderCrudOperations dishOrderCrudOperations;
	private final SaleRestMapper saleRestMapper;

	public List<DishOrder> getBestSales(LocalDate startdate, LocalDate endDate, Integer limit) {
		if (endDate == null) {
			endDate = LocalDate.now();
		}
		if (startdate == null) {
			startdate = LocalDate.of(1000, 1, 1);
		}
		List<DishOrder> dishes =  dishOrderCrudOperations.getBestSales(startdate, endDate);
		return dishes;
	}
}
