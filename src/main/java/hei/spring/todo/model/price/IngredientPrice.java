package hei.spring.todo.model.price;

import hei.spring.todo.model.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static java.time.LocalDate.now;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class IngredientPrice {
	private String id;
	private Ingredient ingredient;
	private Double amount;
	private LocalDate dateValue;

	public IngredientPrice(Double amount) {
		this.amount = amount;
		this.dateValue = now();
	}

	public IngredientPrice(Double amount, LocalDate dateValue) {
		this.amount = amount;
		this.dateValue = dateValue;
	}
}
