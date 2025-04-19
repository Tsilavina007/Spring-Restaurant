package hei.spring.todo.endpoint.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
	private String id;
	private String name;
	private Double currentPrice;
	private Integer quantityOrdered;
	private Status actualOrderStatus;

	@JsonIgnore
	public void getCurrentPrice() {
		return ;
	}
}
