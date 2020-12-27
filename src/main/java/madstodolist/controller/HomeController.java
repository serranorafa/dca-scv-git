package madstodolist.controller;

import madstodolist.model.Usuario;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller

public class HomeController {
    @GetMapping("/about")
    public String about(Model model) {
        return "about";
    }
}
