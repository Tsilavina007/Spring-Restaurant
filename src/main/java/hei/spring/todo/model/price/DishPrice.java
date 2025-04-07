package hei.spring.todo.model.price;

import java.time.LocalDate;

import hei.spring.todo.model.Dish;

public class DishPrice {
	private Double unitPrice;
	private LocalDate updateDatetime;
	private Dish dish;

	public DishPrice(Double unitPrice, LocalDate updateDatetime) {
		this.unitPrice = unitPrice;
		this.updateDatetime = updateDatetime;
	}

	public Dish getDish() {
		return dish;
	}

	public void setDish(Dish dish) {
		this.dish = dish;
	}

	public DishPrice() {
		//TODO Auto-generated constructor stub
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public LocalDate getUpdateDatetime() {
		return updateDatetime;
	}

	public void setUpdateDatetime(LocalDate updateDatetime) {
		this.updateDatetime = updateDatetime;
	}

	@Override
	public String toString() {
		return "DishPrice [unitPrice=" + unitPrice + ", updateDatetime=" + updateDatetime + "]";
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DishPrice other = (DishPrice) obj;
		if (unitPrice != other.unitPrice)
			return false;
		if (updateDatetime == null) {
			if (other.updateDatetime != null)
				return false;
		} else if (!updateDatetime.equals(other.updateDatetime))
			return false;
		return true;
	}



}
