package hei.spring.todo.service;

import hei.spring.todo.dao.operations.IngredientCrudOperations;
import hei.spring.todo.dao.operations.IngredientPriceCrudOperations;
import hei.spring.todo.model.Ingredient;
import hei.spring.todo.model.IngredientPrice;
import hei.spring.todo.model.Price;
import hei.spring.todo.service.exception.ClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientService {
	private final IngredientCrudOperations ingredientCrudOperations;
	private final IngredientPriceCrudOperations ingredientPriceCrudOperations;

	public List<Ingredient> getIngredientsByPrices(Integer page, Integer size, Double priceMinFilter, Double priceMaxFilter) {
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
			return ingredientCrudOperations.getAll(page, size);
		}
		// TODO : paginate from restController OR filter from repository directly
		List<Ingredient> ingredients = ingredientCrudOperations.getAll(1, 500);

		return ingredients.stream()
				.filter(ingredient -> {
					if (priceMinFilter == null && priceMaxFilter == null) {
						return true;
					}
					Double unitPrice = ingredient.getActualPrice();
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

	public List<Ingredient> getAll(Integer page, Integer size) {
		return ingredientCrudOperations.getAll(page, size);
	}

	public Ingredient getById(String id) {
		return ingredientCrudOperations.findById(id);
	}

	public List<Ingredient> saveAll(List<Ingredient> ingredients) {
		return ingredientCrudOperations.saveAll(ingredients);
	}

	public Ingredient addPrices(String ingredientId, List<IngredientPrice> pricesToAdd) {
		System.out.println(pricesToAdd);
		if (pricesToAdd.size() < 1 || pricesToAdd.size() == 1 && pricesToAdd.get(0).equals(new IngredientPrice())) {
			return null;
		}
		Ingredient ingredient = ingredientCrudOperations.findById(ingredientId);
		pricesToAdd.forEach(price -> {
			if (price.getDateValue() == null) {
				price.setDateValue(LocalDate.now());
			}
		});
		pricesToAdd.forEach(price -> price.setIngredient(ingredient));
		List<IngredientPrice> ingredientPriceSaved = ingredientPriceCrudOperations.saveAll(pricesToAdd);
		System.out.println(ingredientPriceSaved);
		if (ingredientPriceSaved.size() > 0) {
			ingredient.addPrices(ingredientPriceSaved);
			return ingredient;
		}
		return null;
	}
}
