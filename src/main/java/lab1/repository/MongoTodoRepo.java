package lab1.repository;

import lab1.models.Todo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface MongoTodoRepo extends MongoRepository<Todo, String>, TodoRepo {

    @Override
    default List<Todo> getAll() {
        return findAll();
    }

    @Override
    default Optional<Todo> getBy(String id) {
        return findById(id);
    }

    @Override
    default void deleteBy(String id) {
        findById(id).ifPresent(this::delete);
    }

    @Override
    default void persist(Todo todo) {
        save(todo);
    }
}
