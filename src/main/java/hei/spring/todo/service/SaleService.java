package hei.spring.todo.service;

import hei.spring.todo.dao.operations.DishOrderCrudOperations;
import hei.spring.todo.endpoint.mapper.SaleRestMapper;
import hei.spring.todo.model.DishOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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
		dishes = dishes.stream()
			.collect(Collectors.toMap(
				dishOrder -> dishOrder.getDish().getIdDish(),
				dishOrder -> dishOrder,
				(dishOrder1, dishOrder2) -> dishOrder1.getQuantity() > dishOrder2.getQuantity() ? dishOrder1 : dishOrder2
			))
			.values()
			.stream()
			.sorted((dishOrder1, dishOrder2) -> dishOrder2.getQuantity() - dishOrder1.getQuantity())
			.toList();
		return dishes;
	}
}
