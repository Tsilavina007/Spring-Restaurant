package hei.spring.todo.endpoint.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Data
public class OrderDishInput {
	private String idDish;
	private Integer quantity;
}
