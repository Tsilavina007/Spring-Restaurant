package hei.spring.todo.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Order {
	private String idOrder;
	private Status status;
	private Instant createdAt;
	private Instant confirmedAt;
	private Instant inPreparationAt;
	private Instant completedAt;
	private Instant deliveredAt;
	private Instant canceledAt;
	private List<DishOrder> listDish;

	public Order(String idOrder) {
		this.idOrder = idOrder;
		this.status = Status.CREATED;
		this.createdAt = Instant.now();
		this.listDish = new ArrayList<>();

	}

	public Order(String idOrder, List<DishOrder> listDish) {
		this.idOrder = idOrder;
		this.status = Status.CREATED;
		this.createdAt = Instant.now();
		this.listDish =  listDish;
	}

	public List<StatusDate> getListStatus() {
		List<StatusDate> listStatusDate = List.of(
			new StatusDate(Status.CREATED, createdAt),
			new StatusDate(Status.CONFIRMED, confirmedAt),
			new StatusDate(Status.IN_PREPARATION, inPreparationAt),
			new StatusDate(Status.COMPLETED, completedAt),
			new StatusDate(Status.DELIVERED, deliveredAt),
			new StatusDate(Status.CANCELED, canceledAt)
		);
		return listStatusDate;
	}

	public List<DishOrder> getDishOrders() {
		return listDish;
	}

	public Status getActualStatus() {
		return status;
	}

	public double getTotalAmount() {
		double totalAmount = 0.0;
		for (DishOrder dishOrder : listDish) {
			totalAmount += dishOrder.getDish().getUnitPrice() * dishOrder.getQuantity();
		}
		return totalAmount;
	}

	public void addDishOrders(List<DishOrder> newListDishOrder) {
		if (this.status != Status.CREATED) {
			throw new RuntimeException("Order is not in CREATED status");
		} else {
			this.listDish.addAll(newListDishOrder);
		}
	}

	public void addDishOrder(DishOrder newDishOrder) {
		if (this.status != Status.CREATED) {
			throw new RuntimeException("Order is not in CREATED status");
		} else {
			this.listDish.add(newDishOrder);
		}
	}

	public void RemoveDishOrderFromOrder(DishOrder dishOrder) {
		if (this.status != Status.CREATED) {
			throw new RuntimeException("Order is not in CREATED status");
		} else {
			this.listDish.remove(dishOrder);
		}
	}

	public void confirm() {
		this.status = Status.CONFIRMED;
		this.confirmedAt = Instant.now();
	}

	public void inPreparation() {
		this.status = Status.IN_PREPARATION;
		this.inPreparationAt = Instant.now();
	}

	public void complete() {
		this.status = Status.COMPLETED;
		this.completedAt = Instant.now();
	}

	public void deliver() {
		this.status = Status.DELIVERED;
		this.deliveredAt = Instant.now();
	}

	public void cancel() {
		this.status = Status.CANCELED;
		this.canceledAt = Instant.now();
	}
}
