package imdb.webservice.controller;

import imdb.webservice.dto.LoginDto;
import imdb.webservice.service.LoginService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/web-service")
public class LoginController {
    private final LoginService loginService;

    @PostMapping("/login")
    public String handleLogin(@ModelAttribute("loginDto") LoginDto dto, BindingResult result, HttpSession httpSession, Model model) {
        log.info("Attempt to authorize a user: {}", dto.getUsername());
        if(result.hasErrors()){
            return "form-login";
        }
        try {
            String response = loginService.login(dto.getUsername(), dto.getPassword());
            httpSession.setAttribute("jwt", response);
            log.info("Session: {}", httpSession.getAttribute("jwt"));
            return "redirect:/web-service/home";
        } catch (Exception e) {
            model.addAttribute("AuthError", e.getMessage());
            return "form-login";
        }
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginDto", new LoginDto());
        return "form-login";
    }
}
