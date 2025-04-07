package hei.spring.todo.endpoint.mapper;

import hei.spring.todo.dao.operations.IngredientCrudOperations;
import hei.spring.todo.endpoint.rest.CreateDishIngredient;
import hei.spring.todo.endpoint.rest.CreateOrUpdateIngredient;
import hei.spring.todo.endpoint.rest.DishIngredientRest;
import hei.spring.todo.endpoint.rest.PriceRest;
import hei.spring.todo.endpoint.rest.StockMovementRest;
import hei.spring.todo.model.Ingredient;
import hei.spring.todo.service.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DishIngredientRestMapper {
	@Autowired
	private IngredientPriceRestMapper priceRestMapper;
	@Autowired
	private StockMovementRestMapper stockMovementRestMapper;
	@Autowired
	private IngredientCrudOperations ingredientCrudOperations;

	public DishIngredientRest toRest(Ingredient ingredient) {
		List<PriceRest> prices = ingredient.getPrices().stream()
				.map(price -> priceRestMapper.apply(price)).toList();
		List<StockMovementRest> stockMovementRests = ingredient.getStockMovements().stream()
				.map(stockMovement -> stockMovementRestMapper.apply(stockMovement))
				.toList();
		return new DishIngredientRest(ingredient.getId(), ingredient.getName(), ingredient.getRequiredQuantity(), ingredient.getUnit() , prices, stockMovementRests);
	}

	public Ingredient toModel(CreateDishIngredient newIngredient) {
		Ingredient ingredient = new Ingredient();
		ingredient.setId(newIngredient.getIngredientId());
		ingredient.setRequiredQuantity(newIngredient.getRequiredQuantity());
		try {
			Ingredient existingIngredient = ingredientCrudOperations.findById(newIngredient.getIngredientId());
			ingredient.addPrices(existingIngredient.getPrices());
			ingredient.addStockMovements(existingIngredient.getStockMovements());
		} catch (NotFoundException e) {
			ingredient.addPrices(new ArrayList<>());
			ingredient.addStockMovements(new ArrayList<>());
		}
		return ingredient;
	}
}
