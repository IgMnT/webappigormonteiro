package br.edu.iff.ccc.webappigormonteiro.controller.view;

import br.edu.iff.ccc.webappigormonteiro.model.UserSystem;
import br.edu.iff.ccc.webappigormonteiro.service.UserSystemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserSystemController {

    private final UserSystemService service;

    public UserSystemController(UserSystemService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("users", service.listarTodos());
        return "user/list"; // templates/user/list.html
    }

    @GetMapping("/{id}")
    public String detalhar(@PathVariable Long id, Model model) {
        UserSystem user = service.buscarPorId(id).orElse(null);
        model.addAttribute("user", user);
        return "user/detail"; // templates/user/detail.html
    }
}
