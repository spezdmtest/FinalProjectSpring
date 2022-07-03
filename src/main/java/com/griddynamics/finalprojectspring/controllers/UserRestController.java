package com.griddynamics.finalprojectspring.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.griddynamics.finalprojectspring.entities.User;
import com.griddynamics.finalprojectspring.services.UserService;
import com.griddynamics.finalprojectspring.services.UserServiceImpl;
import java.util.List;

@Tag(name = "User API", description = "Testing")
@RestController
@RequestMapping("/api/v1/user")
public class UserRestController  {

    private UserService service;

    @Autowired
    public void setService(UserServiceImpl service) {
        this.service = service;
    }

    @GetMapping(path = "/{id}/id", produces = "application/json")
    public User findById(@PathVariable("id") Long id) throws NotFoundException {
        return service.findById(id).orElseThrow(NotFoundException::new);
    }

    @GetMapping(path = "/list", produces = "application/json")
    public List<User> findAll() {
        return service.findAll();
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public User createUser(@RequestBody User user) throws Exception {
        if(!service.existById(user.getId())) {
            service.createOrUpdate(user);
            return user;
        }
        throw new Exception();
    }

    @PutMapping(consumes = "application/json", produces = "application/json")
    public User updateUser(@RequestBody User user) throws NotFoundException {
        if (service.existById(user.getId())) {
            service.createOrUpdate(user);
            return user;
        }
        throw new NotFoundException();
    }
    @DeleteMapping (path = "/{id}/id")
    public void  deleteById(@PathVariable("id") Long id) throws NotFoundException {
        service.deleteById(id);
    }

    @ExceptionHandler
    public ResponseEntity<String> notFoundExeptionHandler(NotFoundException e) {
        return new ResponseEntity<>("Entity not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<String> ExistExeptionHandler(Exception e) {
        return new ResponseEntity<>("Exist user", HttpStatus.CONFLICT);
    }
 }
