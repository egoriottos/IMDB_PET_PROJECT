package imdb.webservice.controller;

import imdb.webservice.service.VerificationService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/web-service")
public class VerifyController {
    private final VerificationService service;
    @GetMapping("/verify")
    public String showVerifyPage(@RequestParam String email,
                                 @RequestParam(required = false) String error,
                                 Model model) {
        model.addAttribute("email", email);
        if (error != null) {
            model.addAttribute("errorMessage", "Неверный код подтверждения");
        }
        return "verify";
    }

    @PostMapping("/verify")
    public String handleVerification(@RequestParam String code,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes) {
        try {
            service.verify(code, session);
            return "redirect:/web-service/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка "+ e.getMessage());
            return "redirect:/web-service/register";
        }
    }
}
