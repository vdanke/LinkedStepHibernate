package org.step.linked.step.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.step.linked.step.dto.UserPrivateDTO;
import org.step.linked.step.dto.UserPublicDTO;
import org.step.linked.step.dto.request.UserSaveRequest;
import org.step.linked.step.dto.request.UserUpdateRequest;
import org.step.linked.step.dto.response.UserSaveResponse;
import org.step.linked.step.dto.response.UserUpdateResponse;
import org.step.linked.step.model.User;
import org.step.linked.step.service.UserService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/*
REST API
0. 1 урлу - 1 метод
1. Нескольким урлам - 1 метод
2. Одному урлу - несколько методов
3. Многим урлам - множество методов

Main mapping - /users
POST - /users
GET - /users
GET - /users/{id}
DELETE /users/{id}
PUT - /users{id}
GET - /users/{id}/courses
GET - /users/{id}/courses/{id}
 */

/*
DispatcherServlet
Map<String, Servlet>
/users -> DispatcherServlet -> getValue(/users + method) -> Servlet(request) ->
-> DispatcherServlet(response) -> marshalling -> UI
(Если сущность имеет LAZY поля, то DispatcherServlet выбрасит эксепшн если эти поля отсутствуют)
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final Path path = Paths.get("C:/Users/viele/files").toAbsolutePath().normalize();

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserPublicDTO> getAllUsers() {
        return userService.findAll()
                .stream()
                .map(UserPublicDTO::toUserPublicDTO)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/sort", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserPublicDTO> getAllUsersWithSorting(
            @RequestParam(name = "age") Integer age,
            @RequestParam(name = "direction", required = false, defaultValue = "default") String direction
    ) {
        return userService.findAllWithSorting(age, direction)
                .stream()
                .map(UserPublicDTO::toUserPublicDTO)
                .collect(Collectors.toList());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserSaveResponse addNewUser(@RequestBody UserSaveRequest request) {
        User user = User.builder().username(request.username).password(request.password).age(request.age).build();
        userService.save(user);
        return new UserSaveResponse(user.getId(), user.getUsername());
    }

    @GetMapping(
            value = "/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public UserPrivateDTO getUserById(@PathVariable(name = "id") Optional<String> id) {
        UserPrivateDTO dto = new UserPrivateDTO();
        id.ifPresent(userID -> {
            User userByID = userService.findById(userID);
            BeanUtils.copyProperties(userByID, dto);
        });
        return dto;
    }

    @DeleteMapping("/{id}")
    public String deleteUserById(@PathVariable(name = "id") String id) {
        boolean isDeleted = userService.deleteById(id);
        if (isDeleted) {
            return "Ok";
        }
        return "Not ok";
    }

    @PutMapping("/{id}")
    public UserUpdateResponse updateUserById(
            @RequestBody UserUpdateRequest request,
            @PathVariable(name = "id") String id
    ) {
        User updatedUser = userService.update(User.builder().id(id).username(request.username).build());
        UserUpdateResponse response = new UserUpdateResponse();
        BeanUtils.copyProperties(updatedUser, response);
        return response;
    }
}
