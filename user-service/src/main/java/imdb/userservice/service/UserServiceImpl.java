package imdb.userservice.service;

import imdb.userservice.dto.AuthenticationRequest;
import imdb.userservice.dto.AuthenticationResponse;
import imdb.userservice.dto.CreateUserDto;
import imdb.userservice.dto.UserDto;
import imdb.userservice.entity.User;
import imdb.userservice.entity.enums.Role;
import imdb.userservice.exception.RegistrationException;
import imdb.userservice.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final String USER_NOT_FOUND = "Username not found ";
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper mapper;
    private final RestTemplate restTemplate;

    @Value("${app.urls.code}")
    private String verifyUrl;
    @Override
    @Transactional
    public void createUser(CreateUserDto createUserDto) {
        User user = User.builder()
                .username(createUserDto.getUsername())
                .password(passwordEncoder.encode(createUserDto.getPassword()))
                .email(createUserDto.getEmail())
                .role(Role.USER)
                .age(createUserDto.getAge())
                .number(createUserDto.getNumber())
                .createdAt(LocalDateTime.now())
                .build();

        if (userRepository.existsByUsername(createUserDto.getUsername())) {
            throw new RegistrationException();
        }
        userRepository.save(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        HttpEntity<String> request = new HttpEntity<>(user.getEmail(),headers);

        try {
            restTemplate.postForEntity(verifyUrl,request,String.class);
        } catch (RestClientException e) {
            throw new RegistrationException();
        }

    }

    @Override
    public String confirmEmailAndAuthenticate(String username, String verificationCode) {
        User user = userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("User not found"));
        return jwtService.generateToken(user);
    }

    @Override
    public AuthenticationResponse createAdmin(CreateUserDto createUserDto) {
        User user = User.builder()
                .username(createUserDto.getUsername())
                .password(passwordEncoder.encode(createUserDto.getPassword()))
                .email(createUserDto.getEmail())
                .role(Role.ADMIN)
                .age(createUserDto.getAge())
                .number(createUserDto.getNumber())
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public String authenticate(AuthenticationRequest request, HttpServletResponse response) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND + request.getUsername()));
        return jwtService.generateToken(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<UserDto> getAll() {
        String holder = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        return userRepository.findAll()
                .stream()
                .filter(user -> !user.getUsername().equals(holder))
                .map(user -> mapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }
}
