package lab1.config;

import lab1.repository.*;
import org.springframework.context.annotation.*;

@Configuration
public class AppConfig {

    @Bean
    public TodoRepo todoRepo() {
        return new InMemoryTodoRepo();
    }

//    @Bean
//    public TodoRepo todoRepo(MongoTodoRepo mongoTodoRepo) {
//        return mongoTodoRepo;
//    }
}
