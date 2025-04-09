package hei.spring.todo.endpoint.rest;

import hei.spring.todo.model.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class DishOrderRest {
	private String idDish;
	private String name;
	private Double currentPrice;
	private Integer quantity;
	private Status status;
}
