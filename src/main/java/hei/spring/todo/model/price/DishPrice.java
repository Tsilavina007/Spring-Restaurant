package hei.spring.todo.model.price;

import java.time.LocalDate;

import hei.spring.todo.model.Dish;
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
public class DishPrice {
	private Double unitPrice;
	private LocalDate updateDatetime;
	private String idDish;

	public DishPrice(Double unitPrice, LocalDate updateDatetime) {
		this.unitPrice = unitPrice;
		this.updateDatetime = updateDatetime;
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
}
