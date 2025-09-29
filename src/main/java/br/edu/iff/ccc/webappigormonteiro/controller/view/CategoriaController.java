package br.edu.iff.ccc.webappigormonteiro.controller.view;

import br.edu.iff.ccc.webappigormonteiro.dto.CategoriaDTO;
import br.edu.iff.ccc.webappigormonteiro.exception.BusinessException;
import br.edu.iff.ccc.webappigormonteiro.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/categorias")
@PreAuthorize("hasAnyRole('ADMIN','AUTHOR')")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("categorias", categoriaService.listarTodas());
        model.addAttribute("categoriaForm", new CategoriaDTO());
        return "categoria/list";
    }

    @PostMapping
    public String criar(@Valid @ModelAttribute("categoriaForm") CategoriaDTO form,
                        BindingResult bindingResult,
                        Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categorias", categoriaService.listarTodas());
            return "categoria/list";
        }
        try {
            categoriaService.criar(form);
        } catch (BusinessException ex) {
            bindingResult.rejectValue("nome", "nome.duplicado", ex.getMessage());
            model.addAttribute("categorias", categoriaService.listarTodas());
            return "categoria/list";
        }
        return "redirect:/categorias";
    }
}
