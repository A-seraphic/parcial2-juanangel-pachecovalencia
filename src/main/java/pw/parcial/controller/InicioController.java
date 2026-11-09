package pw.parcial.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InicioController {

    // manda a cada quien a su vista
    @GetMapping("/")
    public String inicio(HttpServletRequest req) {
        if (req.isUserInRole("ADMIN")) return "redirect:/admin.jsp";
        if (req.isUserInRole("MEDICO")) return "redirect:/medico.jsp";
        if (req.isUserInRole("PACIENTE")) return "redirect:/paciente.jsp";
        return "redirect:/sinpermiso.jsp";
    }
}
