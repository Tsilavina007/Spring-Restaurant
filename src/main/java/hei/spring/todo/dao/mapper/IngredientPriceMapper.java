package hei.spring.todo.dao.mapper;

import hei.spring.todo.model.IngredientPrice;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.function.Function;

@Component
public class IngredientPriceMapper implements Function<ResultSet, IngredientPrice> {
	@SneakyThrows
	@Override
	public IngredientPrice apply(ResultSet resultSet) {
		IngredientPrice price = new IngredientPrice();
		price.setAmount(resultSet.getDouble("unit_price"));
		price.setDateValue(resultSet.getDate("update_datetime").toLocalDate());
		return price;
	}
}
