package hei.spring.todo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class StockMovement {
	private String id;
	private Ingredient ingredient;
	private Double quantity;
	private Unit unit;
	private StockMovementType movementType;
	private Instant creationDatetime;
	public StockMovement(String id, StockMovementType movementType, Double quantity, Unit unit) {
		this.id = id;
		this.movementType = movementType;
		this.quantity = quantity;
		this.unit = unit;
	}
}
