package hei.spring.todo.endpoint;

import hei.spring.todo.endpoint.mapper.DishOrderRestMapper;
import hei.spring.todo.endpoint.mapper.OrderRestMapper;
import hei.spring.todo.endpoint.rest.DishOrderRest;
import hei.spring.todo.endpoint.rest.DishOrderToUpdate;
import hei.spring.todo.endpoint.rest.OrderRest;
import hei.spring.todo.endpoint.rest.OrderToUpdate;
import hei.spring.todo.model.DishOrder;
import hei.spring.todo.model.Order;
import hei.spring.todo.service.OrderService;
import hei.spring.todo.service.exception.ClientException;
import hei.spring.todo.service.exception.NotFoundException;
import hei.spring.todo.service.exception.ServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("/TD5")
public class OrderRestController {
	private final OrderService orderService;
	private final OrderRestMapper orderRestMapper;
	private final DishOrderRestMapper dishOrderRestMapper;

	@GetMapping("/orders/{reference}")
	public ResponseEntity<Object> getOrder(@PathVariable String reference) {
		try {
			Order order = orderService.getByReference(reference);
			OrderRest orderRest = orderRestMapper.toRest(order);
			return ResponseEntity.ok().body(orderRest);
		} catch (ClientException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (NotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
		} catch (ServerException e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}

	@PutMapping("/orders/{reference}/dishes")
	public ResponseEntity<Object> updateOrder(
			@PathVariable String reference,
			@RequestBody OrderToUpdate orderToUpdate) {
		try {
			Order order = orderService.updateOrder(reference ,orderToUpdate);
			OrderRest orderRest =  orderRestMapper.toRest(order);
			return ResponseEntity.ok().body(orderRest);
		} catch (ClientException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (NotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
		} catch (ServerException e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}

	@PutMapping("/orders/{reference}/dishes/{dishId}")
	public ResponseEntity<Object> updateDishOrder(
			@PathVariable String reference,
			@PathVariable String dishId,
			@RequestBody DishOrderToUpdate orderToUpdate) {
		try {
			DishOrder order = orderService.updateDishOrder(reference, dishId ,orderToUpdate);
			DishOrderRest orderRest =  dishOrderRestMapper.apply(order);
			return ResponseEntity.ok().body(orderRest);
		} catch (ClientException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (NotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
		} catch (ServerException e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}
}
