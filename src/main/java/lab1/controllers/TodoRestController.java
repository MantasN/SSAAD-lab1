package lab1.controllers;

import io.swagger.annotations.*;
import lab1.models.Todo;
import lab1.repository.TodoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@Api(value = "/api")
@RequestMapping(value = "/api", produces = { MediaType.APPLICATION_JSON_VALUE })
public class TodoRestController {

    private TodoRepo todoRepo;

    @Autowired
    public TodoRestController(TodoRepo todoRepo) {
        this.todoRepo = todoRepo;
    }

    @GetMapping("/todo")
    @ResponseStatus(OK)
    @ApiOperation(value = "Get all TODO's")
    public List<Todo> getTodoList() {
        return todoRepo.getAll();
    }

    @GetMapping("/todo/{id}")
    @ResponseStatus(OK)
    @ApiOperation(value = "Get TODO by ID")
    public Todo getExistingTodo(@PathVariable String id) {
        return todoRepo.getBy(id)
                .orElseThrow(() -> new RuntimeException("Can't find TODO for provided id!"));
    }

    @DeleteMapping("/todo/{id}")
    @ResponseStatus(OK)
    @ApiOperation(value = "Delete TODO by ID")
    public void deleteExistingTodo(@PathVariable String id) {
        todoRepo.deleteBy(id);
    }

    @PostMapping("/todo")
    @ResponseStatus(CREATED)
    @ApiOperation(value = "Create new TODO")
    public ResponseEntity<Object> createTodo(@Valid @RequestBody Todo todo) {
        todo.id = UUID.randomUUID().toString();
        todoRepo.persist(todo);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(todo.id)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/todo/{id}")
    @ResponseStatus(OK)
    @ApiOperation(value = "Update existing TODO by ID")
    public Todo updateExistingTodo(@Valid @RequestBody Todo todo, @PathVariable String id) {
        todo.id = id;
        todoRepo.persist(todo);
        return todo;
    }
}