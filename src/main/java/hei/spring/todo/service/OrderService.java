package hei.spring.todo.service;

import hei.spring.todo.dao.operations.DishOrderCrudOperations;
import hei.spring.todo.dao.operations.OrderCrudOperations;
import hei.spring.todo.dao.operations.StockMovementCrudOperations;
import hei.spring.todo.endpoint.mapper.OrderDishInputRestMapper;
import hei.spring.todo.endpoint.rest.DishOrderToUpdate;
import hei.spring.todo.endpoint.rest.OrderToUpdate;
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
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
	private final OrderCrudOperations orderCrudOperations;
	private final DishOrderCrudOperations dishOrderCrudOperations;
	private final StockMovementCrudOperations stockMovementCrudOperations;
	private final OrderDishInputRestMapper orderDishInputRestMapper;

	public List<Order> getAll() {
		return orderCrudOperations.getAll(0, 0);
	}

	public Order getByReference(String id) {
		return orderCrudOperations.findById(id);
	}

	public Order saveByReference(String id) {
		Order order = new Order(id);
		return orderCrudOperations.save(order);
	}

	public Order updateOrder(String id, OrderToUpdate orderToUpdate) {
		List<DishOrder> dishOrder = orderToUpdate.getDishes().stream()
			.map(dish -> orderDishInputRestMapper.toModel(id , dish)).toList();
		Order order = orderCrudOperations.findById(id);
		order.setListDish(dishOrder);
		if (orderToUpdate.getOrderStatus() == Status.CONFIRMED) {
			if (order.getActualStatus() != Status.CREATED && order.getActualStatus() != Status.CONFIRMED) {
				throw new ClientException("Order status is not created or confirmed");
			}
			int confirmCount = 0;
			for (DishOrder dishOrderToUpdate : order.getListDish()) {
				if (dishOrderToUpdate.getQuantity() > dishOrderToUpdate.getDish().getAvailableQuantity()) {
					throw new ClientException("Not enough quantity of dish : " + dishOrderToUpdate.getDish().getName());
				} else {
					confirmCount++;
				}
			}
			List<DishOrder> dishOrders = new ArrayList<>();
			if (confirmCount == order.getListDish().size() && order.getActualStatus() == Status.CREATED) {
				for (DishOrder dishOrderToUpdate : order.getListDish()) {
					dishOrders.add(updateDishOrder(id, dishOrderToUpdate.getDish().getIdDish(), new DishOrderToUpdate(Status.CONFIRMED)));
				}
			} else {
				dishOrders = order.getListDish();
			}
			order.setListDish(dishOrders);;
			order.confirm();
		}
		return orderCrudOperations.save(order);
	}

	public Order updateDishOrders(String idOrder, String idDish, DishOrderToUpdate dishOrderToUpdate) {
		updateDishOrder(idOrder, idDish, dishOrderToUpdate);
		return orderCrudOperations.findById(idOrder);
	}

	public DishOrder updateDishOrder(String idOrder, String idDish, DishOrderToUpdate dishOrderToUpdate) {
		List<DishOrder> listDishOrder = dishOrderCrudOperations.getAllDishOrderByidOrder(idOrder);
		DishOrder dishOrder = dishOrderCrudOperations.findByIdDishAndIdOrder(idDish, idOrder);
		if (dishOrderToUpdate.getStatus() == Status.CONFIRMED) {
			if (dishOrder.getActualStatus() != Status.CREATED && dishOrder.getActualStatus() != Status.CONFIRMED) {
				throw new ClientException("DishOrder is not in CREATED or CONFIRMED status");
			}
			if (dishOrder.getQuantity() > dishOrder.getDish().getAvailableQuantity()) {
				throw new ClientException("Not enough quantity of dish : " + dishOrder.getDish().getName());
			}
			List<StockMovement> stockMovements = new ArrayList<>();
			dishOrder.getDish().getIngredients().forEach(ingredient -> {
				stockMovements.add(new StockMovement(
					java.util.UUID.randomUUID().toString(),
					ingredient,
					Math.ceil(dishOrder.getQuantity() * ingredient.getRequiredQuantity() * 100) / 100,
					ingredient.getUnit(),
					StockMovementType.OUT,
					Instant.now()
					));
			});

			dishOrder.confirm();

			DishOrder save = dishOrderCrudOperations.save(dishOrder);
			if (save != null) {
				stockMovementCrudOperations.saveAll(stockMovements);
				int n = 1;
				for (DishOrder elm : listDishOrder) {
					if (elm.getActualStatus() == Status.CONFIRMED) {
						n++;
					}
				}
				if (n == listDishOrder.size()) {
					Order orderToUpdate = orderCrudOperations.findById(idOrder);
					orderToUpdate.confirm();
					orderCrudOperations.save(orderToUpdate);
				}
				return save;
			} else {
				throw new ServerException("DishOrder not saved");
			}
		} else if (dishOrderToUpdate.getStatus() == Status.CANCELED) {
			int n = 1;
				for (DishOrder elm : listDishOrder) {
					if (elm.getActualStatus() == Status.CANCELED) {
						n++;
					}
				}
				if (n == listDishOrder.size()) {
					Order orderToUpdate = orderCrudOperations.findById(idOrder);
					orderToUpdate.cancel();
					orderCrudOperations.save(orderToUpdate);
				}
			dishOrder.cancel();
		} else if (dishOrderToUpdate.getStatus() == Status.IN_PREPARATION) {
			if (dishOrder.getActualStatus() != Status.CONFIRMED && dishOrder.getActualStatus() != Status.IN_PREPARATION) {
				throw new ClientException("DishOrder is not in CONFIRMED status");
			}
			int n = 1;
			for (DishOrder elm : listDishOrder) {
				if (elm.getActualStatus() == Status.IN_PREPARATION) {
					n++;
				}
			}
			if (n == listDishOrder.size()) {
				Order orderToUpdate = orderCrudOperations.findById(idOrder);
				orderToUpdate.inPreparation();
				orderCrudOperations.save(orderToUpdate);
			}
			dishOrder.inPreparation();
		} else if (dishOrderToUpdate.getStatus() == Status.COMPLETED) {
			if (dishOrder.getActualStatus() != Status.IN_PREPARATION && dishOrder.getActualStatus() != Status.COMPLETED) {
				throw new ClientException("DishOrder is not in IN_PREPARATION status");
			}
			int n = 1;
			for (DishOrder elm : listDishOrder) {
				if (elm.getActualStatus() == Status.COMPLETED) {
					n++;
				}
			}
			if (n == listDishOrder.size()) {
				Order orderToUpdate = orderCrudOperations.findById(idOrder);
				orderToUpdate.complete();
				orderCrudOperations.save(orderToUpdate);
			}
			dishOrder.complete();
		} else if (dishOrderToUpdate.getStatus() == Status.DELIVERED) {
			if (dishOrder.getActualStatus() != Status.COMPLETED && dishOrder.getActualStatus() != Status.DELIVERED) {
				throw new ClientException("DishOrder is not in COMPLETED status");
			}
			int n = 1;
			for (DishOrder elm : listDishOrder) {
				if (elm.getActualStatus() == Status.DELIVERED) {
					n++;
				}
			}
			if (n == listDishOrder.size()) {
				Order orderToUpdate = orderCrudOperations.findById(idOrder);
				orderToUpdate.deliver();
				orderCrudOperations.save(orderToUpdate);
			}
			dishOrder.deliver();
		}
		// DishOrder dishOrder = orderDishInputRestMapper.updateToModel(idOrder, idDish, dishOrderToUpdate.getStatus());
		return dishOrderCrudOperations.save(dishOrder);
	}

	// public List<Ingredient> saveAll(List<Ingredient> ingredients) {
	// 	return ingredientCrudOperations.saveAll(ingredients);
	// }
}
