package hei.spring.todo.endpoint;

import hei.spring.todo.endpoint.mapper.DishIngredientRestMapper;
import hei.spring.todo.endpoint.mapper.DishRestMapper;
import hei.spring.todo.endpoint.mapper.IngredientRestMapper;
import hei.spring.todo.endpoint.rest.CreateDishIngredient;
import hei.spring.todo.endpoint.rest.CreateOrUpdateIngredient;
import hei.spring.todo.endpoint.rest.DishRest;
import hei.spring.todo.endpoint.rest.IngredientRest;
import hei.spring.todo.endpoint.rest.DishIngredientRest;
import hei.spring.todo.model.Dish;
import hei.spring.todo.model.Ingredient;
import hei.spring.todo.service.DishService;
import hei.spring.todo.service.IngredientService;
import hei.spring.todo.service.exception.ClientException;
import hei.spring.todo.service.exception.NotFoundException;
import hei.spring.todo.service.exception.ServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("/TD5")
public class DishRestController {
	private final IngredientService ingredientService;
	private final DishService dishService;
	private final IngredientRestMapper ingredientRestMapper;
	private final DishIngredientRestMapper dishIngredientRestMapper;
	private final DishRestMapper dishRestMapper;

	@GetMapping("/dishes")
	public ResponseEntity<Object> getDishes(
			@RequestParam(name = "page", required = false) Integer page,
			@RequestParam(name = "size", required = false) Integer size,
			@RequestParam(name = "priceMinFilter", required = false) Double priceMinFilter,
			@RequestParam(name = "priceMaxFilter", required = false) Double priceMaxFilter) {
		try {
			List<Dish> dishesByPrices = dishService.getDishesByPrices(page, size, priceMinFilter,
					priceMaxFilter);
			List<DishRest> dishRests = dishesByPrices.stream()
					.map(dish -> dishRestMapper.toRest(dish))
					.toList();
			return ResponseEntity.ok().body(dishRests);
		} catch (ClientException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (NotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
		} catch (ServerException e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}

	@PostMapping("/dishes")
	public ResponseEntity<Object> addIngredients() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@PutMapping("/dishes")
	public ResponseEntity<Object> updateIngredients(
			@RequestBody List<CreateOrUpdateIngredient> ingredientsToCreateOrUpdate) {
		try {
			List<Ingredient> ingredients = ingredientsToCreateOrUpdate.stream()
					.map(ingredient -> ingredientRestMapper.toModel(ingredient))
					.toList();
			List<IngredientRest> ingredientsRest = ingredientService.saveAll(ingredients).stream()
					.map(ingredient -> ingredientRestMapper.toRest(ingredient))
					.toList();
			return ResponseEntity.ok().body(ingredientsRest);
		} catch (ServerException e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}

	@PutMapping("/dishes/{idDish}/ingredients")
	public ResponseEntity<Object> addIngredients(
			@PathVariable String idDish,
			@RequestBody List<CreateDishIngredient> dishIngredientsToAdd) {
		try {
			List<DishIngredientRest> ingredientsRest = dishService.addIngredients(idDish ,dishIngredientsToAdd).stream()
					.map(ingredient -> dishIngredientRestMapper.toRest(ingredient))
					.toList();
			return ResponseEntity.ok().body(ingredientsRest);
		} catch (ServerException e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}


	@GetMapping("/dishes/{id}")
	public ResponseEntity<Object> getdish(@PathVariable String id) {
		try {
			return ResponseEntity.ok().body(dishRestMapper.toRest(dishService.getById(id)));
		} catch (ClientException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (NotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
		} catch (ServerException e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}
}
