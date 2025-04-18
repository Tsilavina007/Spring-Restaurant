package hei.spring.todo.endpoint;

import hei.spring.todo.endpoint.mapper.IngredientRestMapper;
import hei.spring.todo.endpoint.rest.CreateIngredientPrice;
import hei.spring.todo.endpoint.rest.CreateOrUpdateIngredient;
import hei.spring.todo.endpoint.rest.CreateStockMovement;
import hei.spring.todo.endpoint.rest.IngredientRest;
import hei.spring.todo.model.Ingredient;
import hei.spring.todo.model.price.IngredientPrice;
import hei.spring.todo.model.StockMovement;
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
public class IngredientRestController {
	private final IngredientService ingredientService;
	private final IngredientRestMapper ingredientRestMapper;

	@GetMapping("/ingredients")
	public ResponseEntity<Object> getIngredients(
			@RequestParam(name = "page", required = false) Integer page,
			@RequestParam(name = "size", required = false) Integer size,
			@RequestParam(name = "priceMinFilter", required = false) Double priceMinFilter,
			@RequestParam(name = "priceMaxFilter", required = false) Double priceMaxFilter) {
		try {
			List<Ingredient> ingredientsByPrices = ingredientService.getIngredientsByPrices(page, size, priceMinFilter,
					priceMaxFilter);
			List<IngredientRest> ingredientRests = ingredientsByPrices.stream()
					.map(ingredient -> ingredientRestMapper.toRest(ingredient))
					.toList();
			return ResponseEntity.ok().body(ingredientRests);
		} catch (ClientException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (NotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
		} catch (ServerException e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}

	@PostMapping("/ingredients")
	public ResponseEntity<Object> addIngredients() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@PutMapping("/ingredients")
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
		} catch (ClientException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (NotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
		} catch (ServerException e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}

	@PutMapping("/ingredients/{ingredientId}/prices")
	public ResponseEntity<Object> updateIngredientPrices(@PathVariable String ingredientId,
			@RequestBody List<CreateIngredientPrice> ingredientPrices) {
		try {
			List<IngredientPrice> prices = ingredientPrices.stream()
					.map(ingredientPrice -> new IngredientPrice(ingredientPrice.getAmount(),
							ingredientPrice.getDateValue()))
					.toList();
			Ingredient ingredient = ingredientService.addPrices(ingredientId, prices);
			if (ingredient != null) {
				IngredientRest ingredientRest = ingredientRestMapper.toRest(ingredient);
				return ResponseEntity.ok().body(ingredientRest);
			}
			return ResponseEntity.status(200).body("No price to add into Ingredient with id : " + ingredientId);
		} catch (ClientException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (NotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
		} catch (ServerException e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}

	@PutMapping("/ingredients/{ingredientId}/stockMovements")
	public ResponseEntity<Object> SaveOrUpdateStockMovements(@PathVariable String ingredientId,
			@RequestBody List<CreateStockMovement> stockMovements) {

		try {
			List<StockMovement> stocks = stockMovements.stream()
					.map(stockMovement -> new StockMovement(stockMovement.getId(), stockMovement.getType(),
							stockMovement.getQuantity(), stockMovement.getUnit()))
					.toList();
			Ingredient ingredient = ingredientService.addStocks(ingredientId, stocks);
			if (ingredient != null) {
				IngredientRest ingredientRest = ingredientRestMapper.toRest(ingredient);
				return ResponseEntity.ok().body(ingredientRest);
			}
			return ResponseEntity.status(200).body("No Stock to add into Ingredient with id : " + ingredientId);
		} catch (ClientException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (NotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
		} catch (ServerException e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}

	@GetMapping("/ingredients/{id}")
	public ResponseEntity<Object> getIngredient(@PathVariable String id) {
		try {
			return ResponseEntity.ok().body(ingredientRestMapper.toRest(ingredientService.getById(id)));
		} catch (ClientException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (NotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
		} catch (ServerException e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}
}
