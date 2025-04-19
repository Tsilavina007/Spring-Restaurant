package hei.spring.todo.endpoint.rest;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@AllArgsConstructor
@Data
@ToString
public class CreateOrUpdateDish {
	private String id;
	private String name;
	private Double unitPrice;
	private LocalDate updateAt;
}
