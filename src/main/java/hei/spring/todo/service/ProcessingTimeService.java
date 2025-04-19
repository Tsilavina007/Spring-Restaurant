package hei.spring.todo.service;

import hei.spring.todo.dao.operations.DishOrderCrudOperations;
import hei.spring.todo.endpoint.mapper.SaleRestMapper;
import hei.spring.todo.model.DishOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProcessingTimeService {
	private final DishOrderCrudOperations dishOrderCrudOperations;
	private final SaleRestMapper saleRestMapper;

	public List<DishOrder> getBestProcessingTime(LocalDate startdate, LocalDate endDate, Integer limit) {
		if (endDate == null) {
			endDate = LocalDate.now();
		}
		if (startdate == null) {
			startdate = LocalDate.of(1000, 1, 1);
		}
		List<DishOrder> dishes =  dishOrderCrudOperations.getSales(startdate, endDate);
		dishes = dishes.stream()
			.sorted((dishOrder1, dishOrder2) -> Double.compare(dishOrder2.getProcessingTime(), dishOrder1.getProcessingTime()))
			.toList();
		return dishes;
	}
}
