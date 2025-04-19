package hei.spring.todo.service;

import hei.spring.todo.dao.operations.DishCrudOperations;
import hei.spring.todo.dao.operations.DishIngredientCrudOperations;
import hei.spring.todo.dao.operations.IngredientCrudOperations;
import hei.spring.todo.dao.operations.StockMovementCrudOperations;
import hei.spring.todo.endpoint.mapper.DishIngredientRestMapper;
import hei.spring.todo.endpoint.rest.CreateDishIngredient;
import hei.spring.todo.model.Dish;
import hei.spring.todo.model.DishIngredient;
import hei.spring.todo.model.Ingredient;
import hei.spring.todo.model.StockMovement;
import hei.spring.todo.service.exception.ClientException;
import hei.spring.todo.service.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DishService {
	private final IngredientCrudOperations ingredientCrudOperations;
	private final DishCrudOperations dishCrudOperations;
	private final DishIngredientCrudOperations dishIngredientCrudOperations;
	private final StockMovementCrudOperations stockMovementCrudOperations;
	private final DishIngredientRestMapper dishIngredientRestMapper;

	public List<Dish> getDishesByPrices(Integer page, Integer size, Double priceMinFilter, Double priceMaxFilter) {
		if (priceMinFilter != null && priceMinFilter < 0) {
			throw new ClientException("PriceMinFilter " + priceMinFilter + " is negative");
		}
		if (priceMaxFilter != null && priceMaxFilter < 0) {
			throw new ClientException("PriceMaxFilter " + priceMaxFilter + " is negative");
		}
		if (priceMinFilter != null && priceMaxFilter != null) {
			if (priceMinFilter > priceMaxFilter) {
				throw new ClientException(
						"PriceMinFilter " + priceMinFilter + " is greater than PriceMaxFilter " + priceMaxFilter);
			}
		}
		if (page == null || size == null) {
			page = 1;
			size = 10;
		}

		if (priceMinFilter == null && priceMaxFilter == null) {
			return dishCrudOperations.getAll(page, size);
		}

		List<Dish> dishes = dishCrudOperations.getAll(1, 500);

		return dishes.stream()
				.filter(dish -> {
					if (priceMinFilter == null && priceMaxFilter == null) {
						return true;
					}
					Double unitPrice = dish.getUnitPrice();
					if (priceMinFilter != null && priceMaxFilter == null) {
						return unitPrice >= priceMinFilter;
					}
					if (priceMinFilter == null) {
						return unitPrice <= priceMaxFilter;
					}
					return unitPrice >= priceMinFilter && unitPrice <= priceMaxFilter;
				})
				.toList();
	}

	public List<Dish> getAll(Integer page, Integer size) {
		return dishCrudOperations.getAll(page, size);
	}

	public Dish getById(String id) {
		return dishCrudOperations.findById(id);
	}

	public List<Ingredient> saveAll(List<Ingredient> ingredients) {
		return ingredientCrudOperations.saveAll(ingredients);
	}

	public List<Ingredient> addIngredients(String idDish, List<CreateDishIngredient> ingredientToAdd) {
		if (ingredientToAdd.size() < 1 || ingredientToAdd.size() == 1 && ingredientToAdd.get(0).equals(new Ingredient())) {
			return null;
		}
		List<DishIngredient> dishIngredientsToAdd = new ArrayList<>();
		List<Ingredient> ingredients = new ArrayList<>();
		ingredientToAdd.forEach(ingredient -> {
			try {
				ingredientCrudOperations.findById(ingredient.getId());
			} catch (NotFoundException e) {
				ingredientCrudOperations.saveAll(List.of(dishIngredientRestMapper.toModel(ingredient)));
			}
			dishIngredientsToAdd.add(new DishIngredient(idDish, ingredient.getId(), ingredient.getRequiredQuantity(), ingredient.getUnit()));
		});
		List<DishIngredient> dishIngredientsSaved = dishIngredientCrudOperations.saveAll(dishIngredientsToAdd);
		// System.out.println(dishIngredientsSaved);
		if (dishIngredientsSaved.size() > 0) {
			dishIngredientsSaved.forEach(ingredient -> {
				Ingredient newIngredient = ingredientCrudOperations.findById(ingredient.getIdIngredient());
				newIngredient.setRequiredQuantity(ingredient.getRequiredQuantity());
				newIngredient.setUnit(ingredient.getUnit());
				ingredients.add(newIngredient);
			});
			return ingredients;
		}
		return null;
	}

	public Ingredient addStocks(String ingredientId, List<StockMovement> stocksToAdd) {
		if (stocksToAdd.size() < 1 || stocksToAdd.size() == 1 && stocksToAdd.get(0).equals(new StockMovement())) {
			return null;
		}
		Ingredient ingredient = ingredientCrudOperations.findById(ingredientId);
		stocksToAdd.forEach(stock -> {
			if (stock.getCreationDatetime() == null) {
				stock.setCreationDatetime(Instant.now());
			}
		});

		stocksToAdd.forEach(stock -> stock.setIngredient(ingredient));
		// System.out.println(stocksToAdd);
		List<StockMovement> stockMovementSaved = stockMovementCrudOperations.saveAll(stocksToAdd);
		if (stockMovementSaved.size() > 0) {
			ingredient.addStockMovements(stockMovementSaved);
			return ingredient;
		}
		return null;
	}
}
