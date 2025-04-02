package hei.spring.todo.endpoint.mapper;

import hei.spring.todo.endpoint.rest.StockMovementRest;
import hei.spring.todo.model.StockMovement;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class StockMovementRestMapper implements Function<StockMovement, StockMovementRest> {

	@Override
	public StockMovementRest apply(StockMovement stockMovement) {
		return new StockMovementRest(stockMovement.getId(),
				stockMovement.getQuantity(),
				stockMovement.getUnit(),
				stockMovement.getMovementType(),
				stockMovement.getCreationDatetime());
	}
}
