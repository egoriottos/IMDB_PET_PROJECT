package imdb.userservice.service;

import imdb.userservice.dto.AuthenticationRequest;
import imdb.userservice.dto.AuthenticationResponse;
import imdb.userservice.dto.CreateUserDto;
import imdb.userservice.dto.UserDto;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

public interface UserService {
    String authenticate(AuthenticationRequest request, HttpServletResponse response);

    void deleteUser(Long id);

    AuthenticationResponse createAdmin(CreateUserDto createUserDto);

    void createUser(CreateUserDto createUserDto);

    String confirmEmailAndAuthenticate(String username, String verificationCode);

    List<UserDto> getAll();
}