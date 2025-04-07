package hei.spring.todo.model;

import java.time.LocalDateTime;
import java.util.List;

public class DishOrder {
	private Dish dish;
	private String idOrder;
	private Integer quantity;
	private Status status;
	private LocalDateTime createdAt;
	private LocalDateTime confirmedAt;
	private LocalDateTime inPreparationAt;
	private LocalDateTime completedAt;
	private LocalDateTime deliveredAt;
	private LocalDateTime canceledAt;

	public DishOrder(String idOrder, Dish dish, Integer quantity) {
		this.idOrder = idOrder;
		this.dish = dish;
		this.quantity = quantity;
		this.status = Status.CREATED;
		this.createdAt = LocalDateTime.now();
	}


	public DishOrder() {
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

	public double getPrice() {
		return (double) dish.getUnitPrice() * quantity;
	}

	public void cancel() {
		this.status = Status.CANCELED;
		this.canceledAt = LocalDateTime.now();
	}

	public void confirm() {
		if (this.status == Status.CONFIRMED) {
			return ;
		}
		if (this.status != Status.CREATED) {
			throw new RuntimeException("DishOrder is not in CREATED status");
		} else {
			if (this.getQuantity() > this.getDish().getAvailableQuantity()) {
				throw new RuntimeException("Ingredient quantity is greater than available quantity in dish :" + this.getDish().getName());
			} else {
				List<Ingredient> ingredients = this.dish.getIngredients();
				for (Ingredient ingredient : ingredients) {
					// A faire sur les Arguments de Stockmvm
					StockMovement mvm = new StockMovement();
					// System.out.println(ingredient.getName() + " : " + ingredient.getAvailableQuantity());
					mvm.setId(LocalDateTime.now().toString());
					ingredient.addStockMovements(List.of(mvm));
					// System.out.println(ingredient.getName() + " : " + ingredient.getAvailableQuantity());
					// ingredient.setQuantity(ingredient.getQuantity()  - (this.quantity * ingredient.getRequiredQuantity()));
				}
				this.status = Status.CONFIRMED;
				this.confirmedAt = LocalDateTime.now();
			}
		}
	}

	public void inPreparation() {
		if (this.status != Status.CONFIRMED) {
			throw new RuntimeException("DishOrder is not in CONFIRMED status");
		} else {
			this.status = Status.IN_PREPARATION;
			this.inPreparationAt = LocalDateTime.now();
		}
	}

	public void complete() {
		if (this.status != Status.IN_PREPARATION) {
			throw new RuntimeException("DishOrder is not in IN_PREPARATION status");
		} else {
			this.status = Status.COMPLETED;
			this.completedAt = LocalDateTime.now();
		}
	}

	public void deliver() {
		if (this.status != Status.COMPLETED) {
			throw new RuntimeException("DishOrder is not in COMPLETED status");
		} else {
			this.status = Status.DELIVERED;
			this.deliveredAt = LocalDateTime.now();
		}
	}

	public Status getActualStatus() {
		return status;
	}

	public void setIdOrder(String idOrder) {
		this.idOrder = idOrder;
	}

	public String getIdOrder() {
		return idOrder;
	}

	public Dish getDish() {
		return dish;
	}

	public void setDish(Dish dish) {
		this.dish = dish;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		if (this.status != Status.CREATED) {
			throw new RuntimeException("DishOrder is not in CREATED status");
		} else {
			this.quantity = quantity;
		}
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


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dish == null) ? 0 : dish.hashCode());
		result = prime * result + ((quantity == null) ? 0 : quantity.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
		result = prime * result + ((confirmedAt == null) ? 0 : confirmedAt.hashCode());
		result = prime * result + ((inPreparationAt == null) ? 0 : inPreparationAt.hashCode());
		result = prime * result + ((completedAt == null) ? 0 : completedAt.hashCode());
		result = prime * result + ((deliveredAt == null) ? 0 : deliveredAt.hashCode());
		result = prime * result + ((canceledAt == null) ? 0 : canceledAt.hashCode());
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
		DishOrder other = (DishOrder) obj;
		if (dish == null) {
			if (other.dish != null)
				return false;
		} else if (!dish.equals(other.dish))
			return false;
		if (quantity == null) {
			if (other.quantity != null)
				return false;
		} else if (!quantity.equals(other.quantity))
			return false;
		if (status != other.status)
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
		return true;
	}


	@Override
	public String toString() {
		return "DishOrder [dish=" + dish + ", quantity=" + quantity + ", status=" + status + ", createdAt=" + createdAt
				+ ", confirmedAt=" + confirmedAt + ", inPreparationAt=" + inPreparationAt + ", completedAt="
				+ completedAt + ", deliveredAt=" + deliveredAt + ", canceledAt=" + canceledAt + "]";
	}



}
