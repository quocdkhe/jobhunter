package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.turkraft.springfilter.boot.Filter;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.dto.UserInfoDTO;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.utils.annotation.ApiMessage;
import vn.hoidanit.jobhunter.utils.error.EmailExistException;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    @ApiMessage("Get all users")
    public ResponseEntity<ResultPaginationDTO<List<UserInfoDTO>>> getAllUser(
            @Filter Specification<User> userSpec,
            Pageable pageable) {
        return ResponseEntity.ok().body(userService.getAllUsers(pageable, userSpec));
    }

    @GetMapping("/users/{id}")
    @ApiMessage("Get user by id")
    public ResponseEntity<UserInfoDTO> getUserById(@PathVariable("id") long id) throws NoResourceFoundException {
        return ResponseEntity.ok().body(userService.getById(id));
    }

    @PostMapping("/users")
    @ApiMessage("Create new user")
    public ResponseEntity<UserInfoDTO> createUser(@RequestBody User user) throws EmailExistException {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
    }

    @DeleteMapping("/users/{id}")
    @ApiMessage("Delete a user")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) throws NoResourceFoundException {
        this.userService.deleteUser(id);
        return ResponseEntity.ok().body(null);
    }

    @PutMapping("/users")
    @ApiMessage("Update a user's info")
    public ResponseEntity<UserInfoDTO> updateUserById(@RequestBody User user) throws NoResourceFoundException {
        return ResponseEntity.ok().body(userService.updateUser(user));
    }

}
