package hei.spring.todo.dao.mapper;

import hei.spring.todo.model.StockMovement;
import hei.spring.todo.model.StockMovementType;
import hei.spring.todo.model.Unit;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.function.Function;

@Component
public class StockMovementMapper implements Function<ResultSet, StockMovement> {
	@SneakyThrows
	@Override
	public StockMovement apply(ResultSet resultSet) {
		StockMovement stockMovement = new StockMovement();
		stockMovement.setId(resultSet.getString("id_mvm"));
		stockMovement.setQuantity(resultSet.getDouble("quantity_mvm"));
		stockMovement.setMovementType(StockMovementType.valueOf(resultSet.getString("type_mvm")));
		stockMovement.setUnit(Unit.valueOf(resultSet.getString("unit")));
		stockMovement.setCreationDatetime(resultSet.getTimestamp("update_datetime").toInstant());
		return stockMovement;
	}
}
