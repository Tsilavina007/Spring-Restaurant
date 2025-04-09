package hei.spring.todo.endpoint.rest;

import java.util.List;

import hei.spring.todo.model.Status;
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
public class OrderToUpdate {
	private Status status;
	private List<OrderDishInput> dishes;
}
