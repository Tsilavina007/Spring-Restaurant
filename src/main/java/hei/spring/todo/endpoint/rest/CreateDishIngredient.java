package hei.spring.todo.endpoint.rest;

import hei.spring.todo.model.Unit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.Getter;

@AllArgsConstructor
@Data
@ToString
@Getter
public class CreateDishIngredient {
	private String id;
	private String name;
	private Double requiredQuantity;
	private Unit unit;
}
