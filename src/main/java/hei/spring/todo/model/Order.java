package hei.spring.todo.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
	private String idOrder;
	private Status status;
	private LocalDateTime createdAt;
	private LocalDateTime confirmedAt;
	private LocalDateTime inPreparationAt;
	private LocalDateTime completedAt;
	private LocalDateTime deliveredAt;
	private LocalDateTime canceledAt;
	private List<DishOrder> listDish;

	public Order(String idOrder, Status status, LocalDateTime createdAt, LocalDateTime confirmedAt,
			LocalDateTime inPreparationAt, LocalDateTime completedAt, LocalDateTime deliveredAt, LocalDateTime canceledAt,
			List<DishOrder> listDish) {
		this.idOrder = idOrder;
		this.status = status;
		this.createdAt = createdAt;
		this.confirmedAt = confirmedAt;
		this.inPreparationAt = inPreparationAt;
		this.completedAt = completedAt;
		this.deliveredAt = deliveredAt;
		this.canceledAt = canceledAt;
		this.listDish = listDish;
	}

	public Order(String idOrder) {
		this.idOrder = idOrder;
		this.status = Status.CREATED;
		this.createdAt = LocalDateTime.now();
		this.listDish = new ArrayList<>();

	}

	public Order(String idOrder, List<DishOrder> listDish) {
		this.idOrder = idOrder;
		this.status = Status.CREATED;
		this.createdAt = LocalDateTime.now();
		this.listDish =  listDish;
	}

	public Order() {
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
		int confirmCount = 0;
		for (DishOrder dishOrder : this.listDish) {
			if (dishOrder.getQuantity() > dishOrder.getDish().getAvailableQuantity()) {
				throw new RuntimeException("Ingredient quantity is greater than available quantity in dish :" + dishOrder.getDish().getName());
			} else {
				confirmCount++;
			}
		}
		if (confirmCount == this.listDish.size() && this.status == Status.CREATED) {
			for (DishOrder dishOrder : this.listDish) {
				dishOrder.confirm();
			}
			this.status = Status.CONFIRMED;
			this.confirmedAt = LocalDateTime.now();
		}

	}

	public void inPreparation() {
		if (this.status == Status.CONFIRMED) {
			this.status = Status.IN_PREPARATION;
			this.inPreparationAt = LocalDateTime.now();
		} else {
			throw new RuntimeException("Order is not in CONFIRMED status");
		}
	}

	public void complete() {
		if (this.status == Status.IN_PREPARATION) {
			this.status = Status.COMPLETED;
			this.completedAt = LocalDateTime.now();
		} else {
			throw new RuntimeException("Order is not in IN_PREPARATION status");
		}
	}

	public void deliver() {
		if (this.status == Status.COMPLETED) {
			this.status = Status.DELIVERED;
			this.deliveredAt = LocalDateTime.now();
		} else {
			throw new RuntimeException("Order is not in COMPLETED status");
		}
	}

	public void cancel() {
		this.status = Status.CANCELED;
		this.canceledAt = LocalDateTime.now();
	}

	public String getIdOrder() {
		return idOrder;
	}

	public void setIdOrder(String idOrder) {
		this.idOrder = idOrder;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getConfirmedAt() {
		return confirmedAt;
	}

	public void setConfirmedAt(LocalDateTime confirmedAt) {
		this.confirmedAt = confirmedAt;
	}

	public LocalDateTime getInPreparationAt() {
		return inPreparationAt;
	}

	public void setInPreparationAt(LocalDateTime inPreparationAt) {
		this.inPreparationAt = inPreparationAt;
	}

	public LocalDateTime getCompletedAt() {
		return completedAt;
	}

	public void setCompletedAt(LocalDateTime completedAt) {
		this.completedAt = completedAt;
	}

	public LocalDateTime getDeliveredAt() {
		return deliveredAt;
	}

	public void setDeliveredAt(LocalDateTime deliveredAt) {
		this.deliveredAt = deliveredAt;
	}

	public LocalDateTime getCanceledAt() {
		return canceledAt;
	}

	public void setCanceledAt(LocalDateTime canceledAt) {
		this.canceledAt = canceledAt;
	}

	public List<DishOrder> getListDish() {
		return listDish;
	}

	public void setListDish(List<DishOrder> listDish) {
		this.listDish = listDish;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idOrder == null) ? 0 : idOrder.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
		result = prime * result + ((confirmedAt == null) ? 0 : confirmedAt.hashCode());
		result = prime * result + ((inPreparationAt == null) ? 0 : inPreparationAt.hashCode());
		result = prime * result + ((completedAt == null) ? 0 : completedAt.hashCode());
		result = prime * result + ((deliveredAt == null) ? 0 : deliveredAt.hashCode());
		result = prime * result + ((canceledAt == null) ? 0 : canceledAt.hashCode());
		result = prime * result + ((listDish == null) ? 0 : listDish.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		if (idOrder == null) {
			if (other.idOrder != null)
				return false;
		} else if (!idOrder.equals(other.idOrder))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (createdAt == null) {
			if (other.createdAt != null)
				return false;
		} else if (!createdAt.equals(other.createdAt))
			return false;
		if (confirmedAt == null) {
			if (other.confirmedAt != null)
				return false;
		} else if (!confirmedAt.equals(other.confirmedAt))
			return false;
		if (inPreparationAt == null) {
			if (other.inPreparationAt != null)
				return false;
		} else if (!inPreparationAt.equals(other.inPreparationAt))
			return false;
		if (completedAt == null) {
			if (other.completedAt != null)
				return false;
		} else if (!completedAt.equals(other.completedAt))
			return false;
		if (deliveredAt == null) {
			if (other.deliveredAt != null)
				return false;
		} else if (!deliveredAt.equals(other.deliveredAt))
			return false;
		if (canceledAt == null) {
			if (other.canceledAt != null)
				return false;
		} else if (!canceledAt.equals(other.canceledAt))
			return false;
		if (listDish == null) {
			if (other.listDish != null)
				return false;
		} else if (!listDish.equals(other.listDish))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Order [idOrder=" + idOrder + ", status=" + status + ", createdAt=" + createdAt + ", confirmedAt="
				+ confirmedAt + ", inPreparationAt=" + inPreparationAt + ", completedAt=" + completedAt
				+ ", deliveredAt=" + deliveredAt + ", canceledAt=" + canceledAt + ", listDish=" + listDish + "]";
	}

}
