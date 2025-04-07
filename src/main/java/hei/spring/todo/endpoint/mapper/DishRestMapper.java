package hei.spring.todo.endpoint.mapper;

import hei.spring.todo.dao.operations.IngredientCrudOperations;
import hei.spring.todo.endpoint.rest.CreateOrUpdateIngredient;
import hei.spring.todo.endpoint.rest.DishIngredientRest;
import hei.spring.todo.endpoint.rest.DishRest;
import hei.spring.todo.endpoint.rest.IngredientRest;
import hei.spring.todo.endpoint.rest.PriceRest;
import hei.spring.todo.endpoint.rest.StockMovementRest;
import hei.spring.todo.model.Dish;
import hei.spring.todo.model.Ingredient;
import hei.spring.todo.service.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DishRestMapper {
	@Autowired
	private DishPriceRestMapper priceRestMapper;
	@Autowired
	private DishIngredientRestMapper ingredientRestMapper;
	@Autowired
	private IngredientCrudOperations ingredientCrudOperations;

	public DishRest toRest(Dish dish) {
		List<PriceRest> prices = dish.getDishPrices().stream()
				.map(price -> priceRestMapper.apply(price)).toList();
		// System.out.println(dish);
		List<DishIngredientRest> ingredients = dish.getIngredients().stream()
				.map(ingredient -> ingredientRestMapper.toRest(ingredient)).toList();
		return new DishRest(dish.getIdDish(), dish.getName(), ingredients, prices);
	}

	public Ingredient toModel(CreateOrUpdateIngredient newIngredient) {
		Ingredient ingredient = new Ingredient();
		ingredient.setId(newIngredient.getId());
		ingredient.setName(newIngredient.getName());
		try {
			Ingredient existingIngredient = ingredientCrudOperations.findById(newIngredient.getId());
			ingredient.addPrices(existingIngredient.getPrices());
			ingredient.addStockMovements(existingIngredient.getStockMovements());
		} catch (NotFoundException e) {
			ingredient.addPrices(new ArrayList<>());
			ingredient.addStockMovements(new ArrayList<>());
		}
		return ingredient;
	}
}
