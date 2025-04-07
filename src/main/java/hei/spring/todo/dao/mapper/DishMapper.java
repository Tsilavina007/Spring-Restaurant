package hei.spring.todo.dao.mapper;

import hei.spring.todo.model.Dish;
import hei.spring.todo.model.Ingredient;
import hei.spring.todo.model.price.DishPrice;
import hei.spring.todo.dao.operations.DishPriceCrudOperations;
import hei.spring.todo.dao.operations.DishIngredientCrudOperations;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class DishMapper implements Function<ResultSet, Dish> {
	private final DishPriceCrudOperations priceCrudOperations;
	private final DishIngredientCrudOperations ingredientCrudOperations;

	@SneakyThrows
	@Override
	public Dish apply(ResultSet resultSet) {
		String idDish = resultSet.getString("id_dish");
		List<DishPrice> dishPrices = priceCrudOperations.findByIdDish(idDish);
		List<Ingredient> ingredients = ingredientCrudOperations.findByIdDish(idDish);

		Dish dish = new Dish();
		dish.setIdDish(idDish);
		dish.setIngredients(ingredients);
		dish.setName(resultSet.getString("name"));
		dish.setDishPrices(dishPrices);

		return dish;
	}
}
