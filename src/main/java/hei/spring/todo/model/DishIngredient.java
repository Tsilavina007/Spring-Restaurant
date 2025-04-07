package hei.spring.todo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DishIngredient {
	private String idDish;
	private String idIngredient;
	private Double requiredQuantity;
	private Unit unit;
}
