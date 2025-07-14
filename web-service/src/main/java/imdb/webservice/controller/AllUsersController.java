package imdb.webservice.controller;

import imdb.webservice.dto.UserDto;
import imdb.webservice.service.DeleteGetService;
import imdb.webservice.service.JwtValidationService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/web-service")
public class AllUsersController {
    private final DeleteGetService service;
    private final JwtValidationService jwtValidationService;
    @GetMapping("/users")
    public String getUsers(HttpSession session, Model model) {
        String jwt = jwtValidationService.getTokenFromSession(session);
        if(!jwtValidationService.validateToken(jwt)){
            session.removeAttribute("jwt");
            return "redirect:/web-service/login";
        }
        System.out.println("JWT from session: " + jwt);
        List<UserDto> users = service.getAll(jwt);
        model.addAttribute("users", users);
        return "users";
    }
}
