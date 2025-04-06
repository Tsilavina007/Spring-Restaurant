package hei.spring.todo.endpoint.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@AllArgsConstructor
@Data
@ToString
public class CreateOrUpdateIngredient {
	private String id;
	private String name;
}
