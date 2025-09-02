package br.edu.iff.ccc.webappigormonteiro.controller.view;

import br.edu.iff.ccc.webappigormonteiro.dto.UserSystemDTO;
import br.edu.iff.ccc.webappigormonteiro.entity.UserSystem;
import br.edu.iff.ccc.webappigormonteiro.service.UserSystemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

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
        model.addAttribute("userForm", new UserSystemDTO());
        return "user/list"; // templates/user/list.html
    }

    @GetMapping("/{id}")
    public String detalhar(@PathVariable Long id, Model model) {
        UserSystem user = service.buscarPorId(id).orElse(null);
        model.addAttribute("user", user);
        return "user/detail"; // templates/user/detail.html
    }

    @PostMapping
    public String criar(@Valid @ModelAttribute("userForm") UserSystemDTO form,
                        BindingResult bindingResult,
                        Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("users", service.listarTodos());
            return "user/list"; // Reexibe a lista com erros
        }
        service.criar(new UserSystem(null, form.getNome(), form.getEmail(), form.getStatus(), form.getRole()));
        return "redirect:/users";
    }
}
