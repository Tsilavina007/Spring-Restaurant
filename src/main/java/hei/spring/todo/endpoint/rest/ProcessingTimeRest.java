package hei.spring.todo.endpoint.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ProcessingTimeRest {
	private String dishId;
	private String dishName;
	private Double preparationDuration;
	private String durationUnit;
}
