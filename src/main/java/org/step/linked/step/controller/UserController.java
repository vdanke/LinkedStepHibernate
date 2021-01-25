package org.step.linked.step.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.step.linked.step.dto.UserPrivateDTO;
import org.step.linked.step.dto.UserPublicDTO;
import org.step.linked.step.dto.request.UserSaveRequest;
import org.step.linked.step.dto.request.UserUpdateRequest;
import org.step.linked.step.dto.response.FileUploadResponse;
import org.step.linked.step.dto.response.UserSaveResponse;
import org.step.linked.step.dto.response.UserUpdateResponse;
import org.step.linked.step.model.User;
import org.step.linked.step.service.FileService;
import org.step.linked.step.service.UserService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
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
    private final FileService fileService;

    // /home/*username*/files/*
    @Autowired
    public UserController(UserService userService,
                          @Qualifier("fileServiceImpl") FileService fileService) {
        this.userService = userService;
        this.fileService = fileService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserPublicDTO>> getAllUsers() {
        List<UserPublicDTO> userPublicDTOS = userService.findAll()
                .stream()
                .map(UserPublicDTO::toUserPublicDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userPublicDTOS);
    }

    @GetMapping(value = "/sort", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserPublicDTO>> getAllUsersWithSorting(
            @RequestParam(name = "age") String age,
            @RequestParam(name = "direction", required = false, defaultValue = "default") String direction
    ) {
        int iAge = Integer.parseInt(age);
        List<UserPublicDTO> userPublicDTOList = userService.findAllWithSorting(iAge, direction)
                .stream()
                .map(UserPublicDTO::toUserPublicDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(userPublicDTOList, HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserSaveResponse> addNewUser(@RequestBody UserSaveRequest request) {
        User user = User.builder().username(request.username).password(request.password).age(request.age).build();
        userService.save(user);
        return ResponseEntity.ok(new UserSaveResponse(user.getId(), user.getUsername()));
    }

    @GetMapping(
            value = "/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<UserPrivateDTO> getUserById(@PathVariable(name = "id") Optional<String> id) {
        UserPrivateDTO dto = new UserPrivateDTO();
        id.ifPresent(userID -> {
            User userByID = userService.findById(userID);
            BeanUtils.copyProperties(userByID, dto);
        });
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUserById(@PathVariable(name = "id") String id) {
        boolean isDeleted = userService.deleteById(id);
        if (isDeleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity
                .status(HttpStatus.I_AM_A_TEAPOT)
                .build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserUpdateResponse> updateUserById(
            @RequestBody UserUpdateRequest request,
            @PathVariable(name = "id") String id
    ) {
        User updatedUser = userService.update(User.builder().id(id).username(request.username).build());
        UserUpdateResponse response = new UserUpdateResponse();
        BeanUtils.copyProperties(updatedUser, response);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/files/upload")
    public ResponseEntity<FileUploadResponse> uploadFile(
            @PathVariable(name = "id") String userID,
            @RequestParam(name = "file") MultipartFile file
    ) throws IOException {
        final String originalFileName = file.getOriginalFilename();
        final String resultFilename = fileService.save(
                file.getInputStream(), originalFileName, userID
        );
        return ResponseEntity.ok(new FileUploadResponse(resultFilename));
    }

    @GetMapping("/{id}/files/download/{filename}")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable(name = "filename") String filename,
            @PathVariable(name = "id") String userID
    ) {
        userService.findById(userID);
        Resource resource = fileService.download(filename);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        String.format("attachment; filename=%s", resource.getFilename())
                ).body(resource);
    }

    @GetMapping("/{username}/github/info")
    public ResponseEntity<String> getExistGithubProfile(@PathVariable(name = "username") String username) {
        String profile = userService.fetchExistsGithubProfile(username);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/null")
    public String getNull() {
        String abc = null;
        byte[] bytes = abc.getBytes();
        return "CaBoooom";
    }

    @GetMapping("/go-study-mathematics")
    public String goStudy() {
        int d = 1 / 0;
        return "CaBoooom";
    }

//    @ExceptionHandler
//    public ResponseEntity<ExceptionDescription> handleNumberFormatException(Exception e) {
//        return ResponseEntity
//                .badRequest()
//                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .body(
//                        new ExceptionDescription(
//                                e.getClass().getSimpleName(), e.getLocalizedMessage(), LocalDateTime.now().toString()
//                        )
//                );
//    }
}
