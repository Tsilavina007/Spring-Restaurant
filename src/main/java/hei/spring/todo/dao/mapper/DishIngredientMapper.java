package hei.spring.todo.dao.mapper;

import hei.spring.todo.model.Dish;
import hei.spring.todo.model.DishIngredient;
import hei.spring.todo.model.Ingredient;
import hei.spring.todo.model.price.IngredientPrice;
import hei.spring.todo.model.StockMovement;
import hei.spring.todo.model.Unit;
import hei.spring.todo.dao.operations.DishCrudOperations;
import hei.spring.todo.dao.operations.IngredientCrudOperations;
import hei.spring.todo.dao.operations.IngredientPriceCrudOperations;
import hei.spring.todo.dao.operations.StockMovementCrudOperations;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class DishIngredientMapper implements Function<ResultSet, DishIngredient> {
	@SneakyThrows
	@Override
	public DishIngredient apply(ResultSet resultSet) {
		DishIngredient dishIngredient = new DishIngredient();
		dishIngredient.setIdDish(resultSet.getString("id_dish"));
		dishIngredient.setIdIngredient(resultSet.getString("id_ingredient"));
		dishIngredient.setRequiredQuantity(resultSet.getDouble("required_quantity"));
		dishIngredient.setUnit(Unit.valueOf(resultSet.getString("unit")));
		return dishIngredient;
	}
}
