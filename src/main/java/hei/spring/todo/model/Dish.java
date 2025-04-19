package hei.spring.todo.model;

import java.time.LocalDate;
import java.util.List;

import hei.spring.todo.model.price.DishPrice;
import hei.spring.todo.model.price.IngredientPrice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Dish {
	private String idDish;
	private String name;
	private Double unitPrice;
	private List<Ingredient> ingredients;
	private List<DishPrice> dishPrices;

	public Dish(String idDish, String name, List<Ingredient> ingredients,
			List<DishPrice> dishPrices) {
		this.idDish = idDish;
		this.name = name;
		this.ingredients = ingredients;
		this.dishPrices = dishPrices;
		this.unitPrice = dishPrices.stream()
				.max((price1, price2) -> price1.getUpdateDatetime().compareTo(price2.getUpdateDatetime()))
				.map(DishPrice::getUnitPrice)
				.orElse(unitPrice);
	}

	public Double getUnitPrice() {
		return dishPrices.stream()
		.max((price1, price2) -> price1.getUpdateDatetime().compareTo(price2.getUpdateDatetime()))
		.map(DishPrice::getUnitPrice)
		.orElse(unitPrice);
	}
	public int getAvailableQuantity() {
		double total = this.ingredients.get(0).getAvailableQuantity();
		for (Ingredient ingredient : this.ingredients) {
			if (total > (ingredient.getAvailableQuantity() / ingredient.getRequiredQuantity())) {
				total = ingredient.getAvailableQuantity() / ingredient.getRequiredQuantity();
			}
		}
		return (int) total;
	}

	public DishPrice getDishPriceWithDateTime(List<DishPrice> listDishPrice,
			LocalDate updateDateTime) {
		DishPrice result = null;
		for (DishPrice dishPrice : listDishPrice) {
			{
				if ((dishPrice.getUpdateDatetime().isBefore(updateDateTime))
						|| dishPrice.getUpdateDatetime().isEqual(updateDateTime)) {
					result = dishPrice;
				}
			}
		}
		if (result != null) {
			for (DishPrice dishPrice : listDishPrice) {
				{
					if ((dishPrice.getUpdateDatetime().isBefore(updateDateTime)
							&& dishPrice.getUpdateDatetime().isAfter(result.getUpdateDatetime()))
							|| dishPrice.getUpdateDatetime().isEqual(updateDateTime)) {
						result = dishPrice;
					}
				}
			}
		}
		return result;
	}

	public IngredientPrice getIngredientPriceWithDateTime(List<IngredientPrice> ingredientsPrice,
			LocalDate updateDateTime) {
		IngredientPrice result = null;
		for (IngredientPrice ingredientPrice : ingredientsPrice) {
			{
				if ((ingredientPrice.getDateValue().isBefore(updateDateTime))
						|| ingredientPrice.getDateValue().isEqual(updateDateTime)) {
					result = ingredientPrice;
				}
			}
		}
		if (result != null) {
			for (IngredientPrice ingredientPrice : ingredientsPrice) {
				{
					if ((ingredientPrice.getDateValue().isBefore(updateDateTime)
							&& ingredientPrice.getDateValue().isAfter(result.getDateValue()))
							|| ingredientPrice.getDateValue().isEqual(updateDateTime)) {
						result = ingredientPrice;
					}
				}
			}
		}
		return result;
	}

	public Double getIngredientsCost(List<Ingredient> ingredientsToFind) {
		double total = 0;
		LocalDate updateDateTime = LocalDate.now();
		if (ingredientsToFind == null && this.ingredients != null) {
			total = this.ingredients.stream()
				.mapToDouble(ingredient -> getIngredientPriceWithDateTime(ingredient.getPrices(), updateDateTime)
						.getAmount() * ingredient.getRequiredQuantity())
				.sum();
		} else if (ingredientsToFind != null) {
			total = ingredientsToFind.stream()
				.mapToDouble(ingredient -> getIngredientPriceWithDateTime(ingredient.getPrices(), updateDateTime)
						.getAmount() * ingredient.getRequiredQuantity())
				.sum();
		}
		return (double) total;
	}

	public Double getIngredientsCost(List<Ingredient> ingredientsToFind, LocalDate updateDateTime) {
		double total = 0;
		if (ingredientsToFind == null && this.ingredients != null) {
			total = this.ingredients.stream()
				.mapToDouble(ingredient -> getIngredientPriceWithDateTime(ingredient.getPrices(), updateDateTime)
						.getAmount() * ingredient.getRequiredQuantity())
				.sum();
		} else if (ingredientsToFind != null) {
			total = ingredientsToFind.stream()
				.mapToDouble(ingredient -> getIngredientPriceWithDateTime(ingredient.getPrices(), updateDateTime)
						.getAmount() * ingredient.getRequiredQuantity())
				.sum();
		}
		return (double) total;
	}

	public Double getIngredientsCost(LocalDate updateDateTime) {
		int total = 0;
		if (this.ingredients != null) {
			for (Ingredient ingredient : this.ingredients) {
				total += getIngredientPriceWithDateTime(ingredient.getPrices(), updateDateTime)
						.getAmount() * ingredient.getRequiredQuantity();
			}
		}
		return (double) total;
	}

	public Double getIngredientsCost() {
		int total = 0;
		if (this.ingredients != null) {
			for (Ingredient ingredient : this.ingredients) {
				total += ingredient.getActualPrice() * ingredient.getRequiredQuantity();
			}
		}
		return (double) total;
	}

	public Double getGrossMargin() {
		return this.getUnitPrice() - getIngredientsCost();
	}

	public Double getGrossMargin(LocalDate updateDateTime) {
		return getDishPriceWithDateTime(this.dishPrices, updateDateTime).getUnitPrice()
				- getIngredientsCost(updateDateTime);
	}

	public void addToListIngredient(Ingredient ingredient) {
		this.ingredients.add(ingredient);
	}

	public void addPrice(DishPrice price) {
		this.dishPrices.add(price);
	}
	public void addPrices(List<DishPrice> prices) {
		this.dishPrices.addAll(prices);
	}

	public void addIngredients(List<Ingredient> ingredients) {
		this.ingredients.addAll(ingredients);
	}


}
