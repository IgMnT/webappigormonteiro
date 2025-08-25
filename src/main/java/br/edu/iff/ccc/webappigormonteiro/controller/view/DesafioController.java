package br.edu.iff.ccc.webappigormonteiro.controller.view;

import br.edu.iff.ccc.webappigormonteiro.model.Desafio;
import br.edu.iff.ccc.webappigormonteiro.model.UserSystem;
import br.edu.iff.ccc.webappigormonteiro.service.DesafioService;
import br.edu.iff.ccc.webappigormonteiro.service.UserSystemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/desafios")
public class DesafioController {

    private final DesafioService desafioService;
    private final UserSystemService userSystemService;

    public DesafioController(DesafioService desafioService, UserSystemService userSystemService) {
        this.desafioService = desafioService;
        this.userSystemService = userSystemService;
    }

    @GetMapping
    public String listar(Model model) {
        List<Desafio> desafios = desafioService.listarTodos();
        model.addAttribute("desafios", desafios);
        model.addAttribute("users", userSystemService.listarTodos());
        return "desafio/list"; // templates/desafio/list.html
    }

    @PostMapping
    public String criar(@RequestParam String dificuldade,
                         @RequestParam Long autorId,
                         @RequestParam String linguagemUtilizada) {
        UserSystem autor = userSystemService.buscarPorId(autorId).orElse(null);
        if (autor != null) {
            desafioService.criar(dificuldade, autor, linguagemUtilizada);
        }
        return "redirect:/desafios";
    }

    @PostMapping("/{id}/delete")
    public String deletar(@PathVariable Long id) {
        desafioService.remover(id);
        return "redirect:/desafios";
    }
}
