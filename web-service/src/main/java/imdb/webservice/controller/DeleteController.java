package imdb.webservice.controller;

import imdb.webservice.service.DeleteGetService;
import imdb.webservice.service.JwtValidationService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/web-service")
public class DeleteController {
    private final DeleteGetService service;
    private final JwtValidationService jwtValidationService;

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            String jwt = jwtValidationService.getTokenFromSession(session);
            if(!jwtValidationService.validateToken(jwt)){
                session.removeAttribute("jwt");
                redirectAttributes.addFlashAttribute("errorMessage", "Сессия истекла");
                return "redirect:/web-service/login";
            }
            System.out.println("JWT from session: " + jwt);
            service.deleteUser(id,jwt);
            redirectAttributes.addFlashAttribute("successMessage", "Пользователь успешно удален");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при удалении: " + e.getMessage());
        }
        return "redirect:/web-service/users";
    }
}
