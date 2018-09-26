package lab1.controllers;

import lab1.models.Todo;
import lab1.repository.TodoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.UUID;

@Controller
public class TodoController {

    private TodoRepo todoRepo;

    @Autowired
    public TodoController(TodoRepo todoRepo) {
        this.todoRepo = todoRepo;
    }

    @GetMapping("/")
    public String getTodoList(Model model) {
        model.addAttribute("todos", Todo.findAll(todoRepo));
        return "todo-list";
    }

    @GetMapping("/todo")
    public String getNewTodo(Model model) {
        model.addAttribute("todo", Todo.getNew());
        return "todo-form";
    }

    @GetMapping("/todo/{id}")
    public String getExistingTodo(@PathVariable String id, Model model) {
        Todo todo = Todo.findBy(id, todoRepo)
                .orElseThrow(() -> new RuntimeException("Can't find TODO for provided id!"));

        model.addAttribute("todo", todo);
        return "todo-form";
    }

    @PostMapping("/todo/{id}")
    public String deleteExistingTodo(@PathVariable String id, Model model) {
        Todo.findBy(id, todoRepo).ifPresent(t -> t.delete(todoRepo));
        model.addAttribute("todos", Todo.findAll(todoRepo));
        return "todo-list";
    }

    @PostMapping("/todo")
    public String createTodo(@Valid Todo todo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "todo-form";
        }

        todo.persist(todoRepo);
        return "todo-preview";
    }

    @ModelAttribute("allPriorityValues")
    public Todo.Priority[] getAllPriorityValues() {
        return Todo.Priority.values();
    }
}