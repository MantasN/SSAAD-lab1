package lab1.repository;

import lab1.models.Todo;

import java.util.*;

public class InMemoryTodoRepo implements TodoRepo {

    private Map<String, Todo> todos = new HashMap<>();

    @Override
    public List<Todo> getAll() {
        return new ArrayList<>(todos.values());
    }

    @Override
    public Optional<Todo> getBy(String id) {
        return Optional.ofNullable(todos.get(id));
    }

    @Override
    public void deleteBy(String id) {
        todos.remove(id);
    }

    @Override
    public void persist(Todo todo) {
        this.todos.put(todo.id, todo);
    }
}
