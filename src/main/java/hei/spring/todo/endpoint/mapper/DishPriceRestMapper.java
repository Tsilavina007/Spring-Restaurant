package hei.spring.todo.endpoint.mapper;

import hei.spring.todo.endpoint.rest.PriceRest;
import hei.spring.todo.model.price.DishPrice;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class DishPriceRestMapper implements Function<DishPrice, PriceRest> {

	@Override
	public PriceRest apply(DishPrice price) {
		return new PriceRest( price.getUnitPrice(), price.getUpdateDatetime());
	}
}
