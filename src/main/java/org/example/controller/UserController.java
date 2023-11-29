package org.example.controller;

import org.example.model.ERole;
import org.example.model.User;
import org.example.service.AccountHistoryService;
import org.springframework.web.bind.annotation.*;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final AccountHistoryService accountHistoryService;

    @Autowired
    public UserController(UserService userService, AccountHistoryService accountHistoryService) {
        this.userService = userService;
        this.accountHistoryService = accountHistoryService;
    }

    @PostMapping(value = "/user")
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        User savedUser = this.userService.saveUser(user);

        if(savedUser == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else{
            return new ResponseEntity<>(savedUser, HttpStatus.OK);
        }
    }

    @GetMapping(value = "/user")
    public ResponseEntity<User> getUser(@RequestParam String email){
        User updatedUser = this.userService.getUserByEmail(email);

        if(updatedUser == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else{
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }
    }

    @PutMapping(value = "/user")
    public ResponseEntity<User> updateUser(@RequestParam String email, @RequestParam String password){
        User user = new User();
        user.setPassword(password);
        user.setEmail(email);
        User oldUser = this.userService.getUserByEmail(user.getEmail());
        User updatedUser = this.userService.updateUser(user);
        if(updatedUser == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else{
            this.accountHistoryService.makeNewEntry(oldUser, updatedUser);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }
    }
}
