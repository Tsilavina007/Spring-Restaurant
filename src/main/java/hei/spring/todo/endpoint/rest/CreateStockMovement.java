package hei.spring.todo.endpoint.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import hei.spring.todo.model.StockMovementType;
import hei.spring.todo.model.Unit;

@AllArgsConstructor
@Getter
@Data
public class CreateStockMovement {
	private String id;
	private Double quantity;
	private Unit unit;
	private StockMovementType type;
}
