package hei.spring.todo.endpoint.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@ToString
public class PriceRest {
	public PriceRest(double d) {
		//TODO Auto-generated constructor stub
	}
	private Double price;
	private LocalDate dateValue;
}
