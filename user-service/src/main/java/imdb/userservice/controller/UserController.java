package imdb.userservice.controller;

import imdb.userservice.dto.AuthenticationRequest;
import imdb.userservice.dto.AuthenticationResponse;
import imdb.userservice.dto.ConfirmAuthRequest;
import imdb.userservice.dto.CreateUserDto;
import imdb.userservice.dto.UserDto;
import imdb.userservice.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> login(@RequestBody AuthenticationRequest request,
                                     HttpServletResponse httpResponse) {
        log.info("Start login user: {}", request.getUsername());
        String jwt = userService.authenticate(request, httpResponse);
        log.info("Response: {}", jwt);

        return ResponseEntity.ok().body(Map.of("token", jwt) );
    }

    @PostMapping("/register/user")
    public ResponseEntity<String> register(@RequestBody CreateUserDto dto) {
        userService.createUser(dto);
        return ResponseEntity.ok("Пользователь создан, ожидание подтверждения почты");
    }
    @PostMapping("/token")
    public ResponseEntity<Map<String,String>> getToken(@RequestBody ConfirmAuthRequest confirmAuthRequest) {
        String jwt = userService.confirmEmailAndAuthenticate(confirmAuthRequest.getUsername(), confirmAuthRequest.getVerificationCode());

        return ResponseEntity.ok().body(Map.of("token",jwt));
    }

    @PostMapping("/register/admin")
    public ResponseEntity<AuthenticationResponse> registerAdmin(@RequestBody CreateUserDto dto) {
        AuthenticationResponse response = userService.createAdmin(dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User with id " + id + " was deleted!");
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> allUsers() {
        return ResponseEntity.ok(userService.getAll());
    }
}
