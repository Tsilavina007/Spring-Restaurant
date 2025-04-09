package hei.spring.todo.dao.mapper;

import hei.spring.todo.model.price.DishPrice;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.function.Function;

@Component
public class DishPriceMapper implements Function<ResultSet, DishPrice> {
	@SneakyThrows
	@Override
	public DishPrice apply(ResultSet resultSet) {
		DishPrice price = new DishPrice();
		price.setUnitPrice(resultSet.getDouble("unit_price"));
		price.setUpdateDatetime(resultSet.getDate("update_datetime").toLocalDate());
		return price;
	}
}
