package hei.spring.todo.endpoint.rest;

import java.util.List;

import hei.spring.todo.model.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class OrderRest {
	private String id;
	private Status actualStatus;
	private Double totalAmount;
	private List<DishOrderRest> dishes;
}
