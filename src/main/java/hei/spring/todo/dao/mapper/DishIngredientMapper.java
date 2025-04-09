package hei.spring.todo.dao.mapper;

import hei.spring.todo.model.DishIngredient;
import hei.spring.todo.model.Unit;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
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
