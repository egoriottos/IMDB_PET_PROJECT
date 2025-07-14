package imdb.webservice.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web-service")
@RequiredArgsConstructor
public class HomeController {

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        String token = (String) session.getAttribute("token");
        model.addAttribute("message","Добро пожаловать");
        return "home";
    }
}
