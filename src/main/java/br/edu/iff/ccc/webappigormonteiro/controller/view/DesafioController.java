package br.edu.iff.ccc.webappigormonteiro.controller.view;

import br.edu.iff.ccc.webappigormonteiro.dto.DesafioDTO;
import br.edu.iff.ccc.webappigormonteiro.entity.Desafio;
import br.edu.iff.ccc.webappigormonteiro.entity.UserSystem;
import br.edu.iff.ccc.webappigormonteiro.service.DesafioService;
import br.edu.iff.ccc.webappigormonteiro.service.UserSystemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
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
        model.addAttribute("desafioForm", new DesafioDTO());
        return "desafio/list"; // templates/desafio/list.html
    }

    @PostMapping
    public String criar(@Valid @ModelAttribute("desafioForm") DesafioDTO form,
                        BindingResult bindingResult,
                        Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("desafios", desafioService.listarTodos());
            model.addAttribute("users", userSystemService.listarTodos());
            return "desafio/list";
        }
        UserSystem autor = userSystemService.buscarPorId(form.getAutorId()).orElse(null);
        if (autor != null) {
            desafioService.criar(form.getDificuldade(), autor, form.getLinguagemUtilizada());
        }
        return "redirect:/desafios";
    }

    @PostMapping("/{id}/delete")
    public String deletar(@PathVariable Long id) {
        desafioService.remover(id);
        return "redirect:/desafios";
    }
}
