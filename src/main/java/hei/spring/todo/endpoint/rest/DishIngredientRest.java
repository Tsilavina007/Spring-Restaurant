package hei.spring.todo.endpoint.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;

import hei.spring.todo.model.Unit;

import static hei.spring.todo.model.StockMovementType.IN;
import static hei.spring.todo.model.StockMovementType.OUT;

@AllArgsConstructor
@Getter
public class DishIngredientRest {
	private String idIngredient;
	private String name;
	private Double requiredQuantity;
	private Unit unit;
	private List<PriceRest> prices;
	private List<StockMovementRest> stockMovements;

	@JsonIgnore
	public List<PriceRest> getPrices() {
		return this.prices;
	}

	@JsonIgnore
	public void getStockMovements() {
		return ;
	}
	public Double getActualPrice() {
		if (this.prices.size() == 0) {
			return 0.0;
		}
		return findActualPrice().orElse(new PriceRest(0.0)).getPrice();
	}

	private Optional<PriceRest> findActualPrice() {
		return prices.stream().max(Comparator.comparing(PriceRest::getDateValue));
	}
	public Double getAvailableQuantity() {
		return getAvailableQuantityAt(Instant.now());
	}

	public Double getAvailableQuantityAt(Instant datetime) {
		List<StockMovementRest> stockMovementsBeforeToday = stockMovements.stream()
				.filter(stockMovement -> stockMovement.getCreationDatetime().isBefore(datetime)
						|| stockMovement.getCreationDatetime().equals(datetime))
				.toList();
		double quantity = 0;
		for (StockMovementRest stockMovement : stockMovementsBeforeToday) {
			if (IN.equals(stockMovement.getType())) {
				quantity += stockMovement.getQuantity();
			} else if (OUT.equals(stockMovement.getType())) {
				quantity -= stockMovement.getQuantity();
			}
		}
		return quantity;
	}
}
