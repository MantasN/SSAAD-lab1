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
        model.addAttribute("todos", todoRepo.getAll());
        return "todo-list";
    }

    @GetMapping("/todo")
    public String getNewTodo(Model model) {
        Todo newTodo = new Todo();
        newTodo.id = UUID.randomUUID().toString();
        newTodo.dueDate = LocalDate.now();

        model.addAttribute("todo", newTodo);
        return "todo-form";
    }

    @GetMapping("/todo/{id}")
    public String getExistingTodo(@PathVariable String id, Model model) {
        Todo todo = todoRepo
                .getBy(id)
                .orElseThrow(() -> new RuntimeException("Can't find TODO for provided id!"));

        model.addAttribute("todo", todo);
        return "todo-form";
    }

    @PostMapping("/todo/{id}")
    public String deleteExistingTodo(@PathVariable String id, Model model) {
        todoRepo.deleteBy(id);
        model.addAttribute("todos", todoRepo.getAll());
        return "todo-list";
    }

    @PostMapping("/todo")
    public String createTodo(@Valid Todo todo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "todo-form";
        }

        todoRepo.persist(todo);
        return "todo-preview";
    }

    @ModelAttribute("allPriorityValues")
    public Todo.Priority[] getAllPriorityValues() {
        return Todo.Priority.values();
    }
}