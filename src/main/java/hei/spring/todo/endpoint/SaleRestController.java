package hei.spring.todo.endpoint;

import hei.spring.todo.endpoint.mapper.SaleRestMapper;
import hei.spring.todo.endpoint.rest.SaleRest;
import hei.spring.todo.model.DishOrder;
import hei.spring.todo.service.SaleService;
import hei.spring.todo.service.exception.ClientException;
import hei.spring.todo.service.exception.NotFoundException;
import hei.spring.todo.service.exception.ServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class SaleRestController {
	private final SaleService saleService;
	private final SaleRestMapper saleRestMapper;

	@GetMapping("/sales")
	public ResponseEntity<Object> getBestSales(
		@RequestParam(name = "startDate", required = false) LocalDate startDate,
		@RequestParam(name = "endDate", required = false) LocalDate endDate,
		@RequestParam(name = "limit", required = false) Integer limit) {
		try {
			List<DishOrder> dishesOrder = saleService.getBestSales(startDate, endDate, limit);
			List<SaleRest> saleRests = dishesOrder.stream()
				.map(dishOrder -> saleRestMapper.toRest(dishOrder))
				.toList();
			return ResponseEntity.ok().body(saleRests);
		} catch (ClientException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (NotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
		} catch (ServerException e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}
}
