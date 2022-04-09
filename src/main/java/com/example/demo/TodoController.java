package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "todo", produces = APPLICATION_JSON_VALUE)
public class TodoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TodoController.class);

    private final TodoRepo repo;

    public TodoController(TodoRepo repo) {
        this.repo = repo;
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public Todo create(@RequestBody  Todo todo) {
        LOGGER.info("Incoming Todo {}", todo);
        return repo.save(todo);
    }

    @GetMapping
    public Iterable<Todo> listAll() {
        return repo.findAll();
    }

}
