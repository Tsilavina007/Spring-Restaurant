package hei.spring.todo.endpoint.rest;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;

import hei.spring.todo.model.price.IngredientPrice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@ToString
public class DishRest {
	private String id;
	private String name;
	private List<DishIngredientRest> ingredients;
	private List<PriceRest> dishPrices;

	@JsonIgnore
	public List<PriceRest> getDishPrices() {
		return this.dishPrices;
	}
	public Double getActualPrice() {
		if (dishPrices.size() == 0) {
			return 0.0;
		}
		return findActualPrice().orElse(new PriceRest(0.0)).getPrice();
	}

	private Optional<PriceRest> findActualPrice() {
		return dishPrices.stream().max(Comparator.comparing(PriceRest::getDateValue));
	}

	public int getAvailableQuantity() {
		if (ingredients.size() == 0) {
			return 0;
		}
		double total = this.ingredients.get(0).getAvailableQuantity();
		for (DishIngredientRest ingredient : this.ingredients) {
			if (total > (ingredient.getAvailableQuantity() / ingredient.getRequiredQuantity())) {
				total = ingredient.getAvailableQuantity() / ingredient.getRequiredQuantity();
			}
		}
		return (int) total;
	}

	public PriceRest getDishPriceWithDateTime(List<PriceRest> dishPrices2,
			LocalDate updateDateTime) {
		PriceRest result = null;
		for (PriceRest dishPrice : dishPrices2) {
			{
				if ((dishPrice.getDateValue().isBefore(updateDateTime))
						|| dishPrice.getDateValue().isEqual(updateDateTime)) {
					result = dishPrice;
				}
			}
		}
		if (result != null) {
			for (PriceRest dishPrice : dishPrices2) {
				{
					if ((dishPrice.getDateValue().isBefore(updateDateTime)
							&& dishPrice.getDateValue().isAfter(result.getDateValue()))
							|| dishPrice.getDateValue().isEqual(updateDateTime)) {
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

	@JsonIgnore
	public Double getIngredientsCost() {
		int total = 0;
		if (this.ingredients != null) {
			for (DishIngredientRest ingredient : this.ingredients) {
				total += ingredient.getActualPrice() * ingredient.getRequiredQuantity();
			}
		}
		return (double) total;
	}

	@JsonIgnore
	public Double getGrossMargin() {
		return this.getActualPrice() - getIngredientsCost();
	}
}
