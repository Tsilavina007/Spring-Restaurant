package hei.spring.todo.endpoint.rest;

import hei.spring.todo.model.StockMovementType;
import hei.spring.todo.model.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@AllArgsConstructor
@Getter
public class StockMovementRest {
	private String id;
	private Double quantity;
	private Unit unit;
	private StockMovementType type;
	private Instant creationDatetime;
}
