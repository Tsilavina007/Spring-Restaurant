package hei.spring.todo.model;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class DishOrder {
	private Dish dish;
	private String idOrder;
	private Integer quantity;
	private Status status;
	private Instant createdAt;
	private Instant confirmedAt;
	private Instant inPreparationAt;
	private Instant completedAt;
	private Instant deliveredAt;
	private Instant canceledAt;

	public DishOrder(String idOrder, Dish dish, Integer quantity) {
		this.idOrder = idOrder;
		this.dish = dish;
		this.quantity = quantity;
		this.status = Status.CREATED;
		this.createdAt = Instant.now();
	}

	public List<StatusDate> getListStatus() {
		List<StatusDate> listStatusDate = List.of(
				new StatusDate(Status.CREATED, createdAt),
				new StatusDate(Status.CONFIRMED, confirmedAt),
				new StatusDate(Status.IN_PREPARATION, inPreparationAt),
				new StatusDate(Status.COMPLETED, completedAt),
				new StatusDate(Status.DELIVERED, deliveredAt),
				new StatusDate(Status.CANCELED, canceledAt));
		return listStatusDate;
	}

	public double getPrice() {
		return (double) dish.getUnitPrice() * quantity;
	}

	public void cancel() {
		this.status = Status.CANCELED;
		this.canceledAt = Instant.now();
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

	public Status getActualStatus() {
		return status;
	}

	public void addQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public void setQuantity(Integer quantity) {
		if (this.status != Status.CREATED) {
			throw new RuntimeException("DishOrder is not in CREATED status");
		} else {
			this.quantity = quantity;
		}
	}
}
