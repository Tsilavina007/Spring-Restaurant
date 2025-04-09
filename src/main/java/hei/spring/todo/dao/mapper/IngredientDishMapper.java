package hei.spring.todo.dao.mapper;

import hei.spring.todo.model.Ingredient;
import hei.spring.todo.model.price.IngredientPrice;
import hei.spring.todo.model.StockMovement;
import hei.spring.todo.model.Unit;
import hei.spring.todo.dao.operations.IngredientPriceCrudOperations;
import hei.spring.todo.dao.operations.StockMovementCrudOperations;
import lombok.SneakyThrows;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.List;
import java.util.function.Function;

@Component
public class IngredientDishMapper implements Function<ResultSet, Ingredient> {
	@Autowired
	private IngredientPriceCrudOperations priceCrudOperations;
	@Autowired
	private StockMovementCrudOperations stockMovementCrudOperations;

	@SneakyThrows
	@Override
	public Ingredient apply(ResultSet resultSet) {
		String idIngredient = resultSet.getString("id_ingredient");
		List<IngredientPrice> ingredientPrices = priceCrudOperations.findByIdIngredient(idIngredient);
		List<StockMovement> ingredientStockMovements = stockMovementCrudOperations.findByIdIngredient(idIngredient);

		System.out.println(resultSet.getDouble("required_quantity"));
		Ingredient ingredient = new Ingredient();
		ingredient.setId(idIngredient);
		ingredient.setRequiredQuantity(resultSet.getDouble("required_quantity"));
		ingredient.setUnit(Unit.valueOf(resultSet.getString("unit")));
		ingredient.setName(resultSet.getString("name"));
		ingredient.setPrices(ingredientPrices);
		ingredient.setStockMovements(ingredientStockMovements);
		return ingredient;
	}
}
