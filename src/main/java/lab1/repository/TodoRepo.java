package lab1.repository;

import lab1.models.Todo;

import java.util.*;

public interface TodoRepo {
    List<Todo> getAll();
    Optional<Todo> getBy(String id);
    void deleteBy(String id);
    void persist(Todo todo);
}
