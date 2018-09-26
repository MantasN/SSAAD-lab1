package lab1.models;

import com.fasterxml.jackson.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lab1.repository.TodoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;

@Document
public class Todo {

    @Id
    @ApiModelProperty(hidden = true)
    public String id;

    @NotEmpty
    @ApiModelProperty(example = "TODO Title")
    public String title;

    @Size(max = 120)
    @ApiModelProperty(example = "Description of the TODO")
    public String description;

    @FutureOrPresent
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @ApiModelProperty(example = "2019-01-01")
    public LocalDate dueDate;

    @NotNull
    public Priority priority;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Todo todo = (Todo) o;
        return Objects.equals(id, todo.id) &&
                Objects.equals(title, todo.title) &&
                Objects.equals(description, todo.description) &&
                Objects.equals(dueDate, todo.dueDate) &&
                priority == todo.priority;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, dueDate, priority);
    }

    public void delete(TodoRepo todoRepo) {
        todoRepo.deleteBy(id);
    }

    public void persist(TodoRepo todoRepo) {
        todoRepo.persist(this);
    }

    public static Optional<Todo> findBy(String id, TodoRepo todoRepo) {
        return todoRepo.getBy(id);
    }

    public static List<Todo> findAll(TodoRepo todoRepo) {
        return todoRepo.getAll();
    }

    public static Todo getNew() {
        Todo newTodo = new Todo();
        newTodo.id = UUID.randomUUID().toString();
        newTodo.dueDate = LocalDate.now();

        return newTodo;
    }

    public enum Priority {
        LOW, MEDIUM, HIGH
    }
}
