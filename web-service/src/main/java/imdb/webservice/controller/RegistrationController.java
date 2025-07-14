package imdb.webservice.controller;

import imdb.webservice.dto.RegistrationDto;
import imdb.webservice.service.RegistrationService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/web-service")
public class RegistrationController {
    private final RegistrationService registrationService;

    @PostMapping("/register")
    public String handleLogin(@ModelAttribute RegistrationDto registrationDto, BindingResult bindingResult, Model model,
                              HttpSession session, RedirectAttributes redirectAttributes) {
        log.info("Attempting to register for a user: {}", registrationDto.getUsername());
        if (bindingResult.hasErrors()) {
            return "form-registration";
        }
        try {
            String response = registrationService.registration(
                    registrationDto.getUsername(), registrationDto.getPassword(),
                    registrationDto.getEmail(), registrationDto.getAge(),
                    registrationDto.getNumber()
            );
            session.setAttribute("jwt", response);
            log.info("Session: {}", session.getAttribute("jwt"));
            session.setAttribute("verificationEmail", registrationDto.getEmail());
            session.setAttribute("username", registrationDto.getUsername());
            log.info("User is registered: {}", response);
            redirectAttributes.addAttribute("email", registrationDto.getEmail());
            return "redirect:/web-service/verify";
        } catch (Exception e) {
            model.addAttribute("RegisterError", e.getMessage());
            return "form-registration";
        }
    }
}
