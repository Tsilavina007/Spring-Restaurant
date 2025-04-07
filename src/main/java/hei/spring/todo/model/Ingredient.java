package hei.spring.todo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import hei.spring.todo.model.price.IngredientPrice;

import static hei.spring.todo.model.StockMovementType.IN;
import static hei.spring.todo.model.StockMovementType.OUT;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Ingredient {
	private String id;
	private String name;
	private int requiredQuantity;
	private Unit unit;
	private List<IngredientPrice> prices;
	private List<StockMovement> stockMovements;

	public List<StockMovement> addStockMovements(List<StockMovement> stockMovements) {
		stockMovements.forEach(stockMovement -> stockMovement.setIngredient(this));
		if (getStockMovements() == null || getStockMovements().isEmpty()) {
			return stockMovements;
		}
		getStockMovements().addAll(stockMovements);
		return getStockMovements();
	}

	public List<IngredientPrice> addPrices(List<IngredientPrice> prices) {
		if (getPrices() == null || getPrices().isEmpty()) {
			return prices;
		}
		prices.forEach(price -> price.setIngredient(this));
		getPrices().addAll(prices);
		return getPrices();
	}

	public Double getActualPrice() {
		return findActualPrice().orElse(new IngredientPrice(0.0)).getAmount();
	}

	public Double getAvailableQuantity() {
		return getAvailableQuantityAt(Instant.now());
	}

	public Double getPriceAt(LocalDate dateValue) {
		return findPriceAt(dateValue).orElse(new IngredientPrice(0.0)).getAmount();
	}

	public Double getAvailableQuantityAt(Instant datetime) {
		List<StockMovement> stockMovementsBeforeToday = stockMovements.stream()
				.filter(stockMovement -> stockMovement.getCreationDatetime().isBefore(datetime)
						|| stockMovement.getCreationDatetime().equals(datetime))
				.toList();
		double quantity = 0;
		for (StockMovement stockMovement : stockMovementsBeforeToday) {
			if (IN.equals(stockMovement.getMovementType())) {
				quantity += stockMovement.getQuantity();
			} else if (OUT.equals(stockMovement.getMovementType())) {
				quantity -= stockMovement.getQuantity();
			}
		}
		return quantity;
	}

	private Optional<IngredientPrice> findPriceAt(LocalDate dateValue) {
		return prices.stream()
				.filter(price -> price.getDateValue().equals(dateValue))
				.findFirst();
	}

	private Optional<IngredientPrice> findActualPrice() {
		return prices.stream().max(Comparator.comparing(IngredientPrice::getDateValue));
	}
}
