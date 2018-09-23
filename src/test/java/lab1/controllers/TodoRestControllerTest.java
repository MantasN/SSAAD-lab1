package lab1.controllers;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lab1.models.Todo;
import lab1.repository.*;
import org.junit.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TodoRestControllerTest {

    private static final String BASE_API_URL = "/api/todo/";

    private MockMvc mockMvc;
    private TodoRepo todoRepo;

    @Before
    public void setUp() {
        this.todoRepo = new InMemoryTodoRepo();

        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new TodoRestController(todoRepo))
                .build();
    }

    @Test
    public void shouldReturnEmptyListWhenRepoIsEmpty() throws Exception {
        assertTrue(todoRepo.getAll().isEmpty());

        mockMvc
                .perform(get(BASE_API_URL))
                .andExpect(status().isOk())
                .andExpect(content().json(EMPTY_JSON_ARRAY));
    }

    @Test
    public void shouldCreateTodo() throws Exception {
        Todo todo = getTestTodo();

        mockMvc.perform(post(BASE_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.writeValueAsString(todo)))
                .andExpect(status().isCreated());

        assertEquals(1, todoRepo.getAll().size());

        Todo todoFromRepo = todoRepo.getAll().get(0);

        assertEquals(todo.title, todoFromRepo.title);
        assertEquals(todo.description, todoFromRepo.description);
        assertEquals(todo.dueDate, todoFromRepo.dueDate);
        assertEquals(todo.priority, todoFromRepo.priority);
    }

    @Test
    public void shouldUpdateTodo() throws Exception {
        Todo todo = getTestTodo();

        mockMvc.perform(post(BASE_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.writeValueAsString(todo)))
                .andExpect(status().isCreated());

        assertEquals(1, todoRepo.getAll().size());

        Todo todoFromRepo = todoRepo.getAll().get(0);

        todo.title = "changed title";

        mockMvc.perform(put(BASE_API_URL + todoFromRepo.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.writeValueAsString(todo)))
                .andExpect(status().isOk());

        assertEquals(1, todoRepo.getAll().size());
        Todo updatedTodo = todoRepo.getAll().get(0);

        assertEquals(todo.title, updatedTodo.title);
        assertEquals(todo.description, updatedTodo.description);
        assertEquals(todo.dueDate, updatedTodo.dueDate);
        assertEquals(todo.priority, updatedTodo.priority);
    }


    @Test
    public void shouldRemoveTodo() throws Exception {
        Todo testTodo = getTestTodo();

        String firstTodoId = UUID.randomUUID().toString();
        testTodo.id = firstTodoId;
        todoRepo.persist(testTodo);

        String secondTodoId = UUID.randomUUID().toString();
        testTodo.id = secondTodoId;
        todoRepo.persist(testTodo);

        mockMvc.perform(delete(BASE_API_URL + firstTodoId))
                .andExpect(status().isOk());

        assertEquals(1, todoRepo.getAll().size());

        Todo todoFromRepo = todoRepo.getAll().get(0);

        assertEquals(secondTodoId, todoFromRepo.id);
    }

    @Test
    public void shouldGetTodoList() throws Exception {
        Todo testTodo = getTestTodo();

        testTodo.id = UUID.randomUUID().toString();
        todoRepo.persist(testTodo);

        String expectedContent = OBJECT_MAPPER.writeValueAsString(Collections.singletonList(testTodo));

        mockMvc.perform(get(BASE_API_URL))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedContent));
    }

    @Test
    public void shouldGetTodo() throws Exception {
        Todo testTodo = getTestTodo();

        String firstTodoId = UUID.randomUUID().toString();
        testTodo.id = firstTodoId;
        todoRepo.persist(testTodo);

        String expectedContent = OBJECT_MAPPER.writeValueAsString(testTodo);

        mockMvc.perform(get(BASE_API_URL + firstTodoId))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedContent));
    }

    private Todo getTestTodo() {
        Todo todo = new Todo();

        todo.title = "Title";
        todo.description = "Description";
        todo.dueDate = LocalDate.now();
        todo.priority = Todo.Priority.LOW;
        return todo;
    }

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    private static final String EMPTY_JSON_ARRAY = "[]";
}