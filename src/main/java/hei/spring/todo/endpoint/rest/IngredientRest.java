package hei.spring.todo.endpoint.rest;

import hei.spring.todo.model.StockMovement;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class IngredientRest {
	private String id;
	private String name;
	private List<PriceRest> prices;
	private List<StockMovementRest> stockMovements;
}
