package hei.spring.todo.endpoint;

import hei.spring.todo.endpoint.mapper.DishOrderRestMapper;
import hei.spring.todo.endpoint.mapper.OrderRestMapper;
import hei.spring.todo.endpoint.rest.DishOrderToUpdate;
import hei.spring.todo.endpoint.rest.OrderRest;
import hei.spring.todo.endpoint.rest.OrderToUpdate;
import hei.spring.todo.model.Order;
import hei.spring.todo.service.OrderService;
import hei.spring.todo.service.exception.ClientException;
import hei.spring.todo.service.exception.NotFoundException;
import hei.spring.todo.service.exception.ServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderRestController {
	private final OrderService orderService;
	private final OrderRestMapper orderRestMapper;
	private final DishOrderRestMapper dishOrderRestMapper;

	@GetMapping("/orders")
	public ResponseEntity<Object> getAllOrder() {
		try {
			List<Order> orders = orderService.getAll();
			List<OrderRest> orderRests = orders.stream()
								.map(order -> orderRestMapper.toRest(order))
								.toList();
			// OrderRest orderRest = orderRestMapper.toRest(order);
			return ResponseEntity.ok().body(orderRests);
		} catch (ClientException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (NotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
		} catch (ServerException e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}

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

	@PutMapping("/orders/{reference}")
	public ResponseEntity<Object> saveOrder(@PathVariable String reference) {
		try {
			Order order = orderService.saveByReference(reference);
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
			Order order = orderService.updateDishOrders(reference, dishId ,orderToUpdate);
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
}
