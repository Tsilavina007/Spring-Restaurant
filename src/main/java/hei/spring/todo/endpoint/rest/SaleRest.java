package hei.spring.todo.endpoint.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class SaleRest {
	private String dishName;
	private Integer quantitySold;
	private Double totalAmount;
}
