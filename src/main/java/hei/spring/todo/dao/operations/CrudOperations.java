package hei.spring.todo.dao.operations;

import java.util.List;

public interface CrudOperations<E> {
	List<E> getAll(int page, int size);

	E findById(String id);

	// Both create (if does not exist) or update (if exist) entities
	List<E> saveAll(List<E> entities);
}
