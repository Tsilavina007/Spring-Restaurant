package hei.spring.todo.endpoint.mapper;

import hei.spring.todo.endpoint.rest.PriceRest;
import hei.spring.todo.model.IngredientPrice;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class PriceRestMapper implements Function<IngredientPrice, PriceRest> {

	@Override
	public PriceRest apply(IngredientPrice price) {
		return new PriceRest(price.getId(), price.getAmount(), price.getDateValue());
	}
}
