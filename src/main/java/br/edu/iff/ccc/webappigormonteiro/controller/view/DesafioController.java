package br.edu.iff.ccc.webappigormonteiro.controller.view;

import br.edu.iff.ccc.webappigormonteiro.dto.DesafioDTO;
import br.edu.iff.ccc.webappigormonteiro.entity.Categoria;
import br.edu.iff.ccc.webappigormonteiro.entity.Desafio;
import br.edu.iff.ccc.webappigormonteiro.entity.UserSystem;
import br.edu.iff.ccc.webappigormonteiro.exception.ResourceNotFoundException;
import br.edu.iff.ccc.webappigormonteiro.service.CategoriaService;
import br.edu.iff.ccc.webappigormonteiro.service.DesafioService;
import br.edu.iff.ccc.webappigormonteiro.service.UserSystemService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    private final CategoriaService categoriaService;

    public DesafioController(DesafioService desafioService, UserSystemService userSystemService, CategoriaService categoriaService) {
        this.desafioService = desafioService;
        this.userSystemService = userSystemService;
        this.categoriaService = categoriaService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','AUTHOR','VISITOR')")
    @GetMapping
    public String listar(Model model, Authentication authentication) {
        List<Desafio> desafios = desafioService.listarTodos();
        model.addAttribute("desafios", desafios);
        model.addAttribute("users", userSystemService.listarPossiveisAutores());
        model.addAttribute("categorias", categoriaService.listarTodas());
        model.addAttribute("desafioForm", new DesafioDTO());
        addCurrentUser(model, authentication);
        return "desafio/list"; // templates/desafio/list.html
    }

    @PreAuthorize("hasAnyRole('ADMIN','AUTHOR')")
    @PostMapping
    public String criar(@Valid @ModelAttribute("desafioForm") DesafioDTO form,
                        BindingResult bindingResult,
                        Model model,
                        Authentication authentication) {
        addCurrentUser(model, authentication);
        if (bindingResult.hasErrors()) {
            model.addAttribute("desafios", desafioService.listarTodos());
            model.addAttribute("users", userSystemService.listarPossiveisAutores());
            model.addAttribute("categorias", categoriaService.listarTodas());
            return "desafio/list";
        }
        boolean isAdmin = isAdmin(authentication);
        if (!isAdmin) {
            UserSystem atual = getAuthenticatedUser(authentication);
            form.setAutorId(atual.getId());
        }
        UserSystem autor = userSystemService.buscarPorId(form.getAutorId());
        if (!isAdmin && !autor.getEmail().equalsIgnoreCase(authentication.getName())) {
            throw new AccessDeniedException("Autores só podem cadastrar desafios para si mesmos.");
        }
        Categoria categoria = categoriaService.buscarPorId(form.getCategoriaId());
        desafioService.criar(form, autor, categoria);
        return "redirect:/desafios";
    }

    @PreAuthorize("hasAnyRole('ADMIN','AUTHOR')")
    @PostMapping("/{id}/delete")
    public String deletar(@PathVariable Long id, Authentication authentication) {
        Desafio desafio = desafioService.buscarPorIdOrThrow(id);
        boolean isAdmin = isAdmin(authentication);
        if (!isAdmin) {
            if (desafio.getAutor() == null || !desafio.getAutor().getEmail().equalsIgnoreCase(authentication.getName())) {
                throw new AccessDeniedException("Você só pode excluir os seus próprios desafios.");
            }
        }
        desafioService.remover(id);
        return "redirect:/desafios";
    }

    private void addCurrentUser(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            userSystemService.buscarPorEmail(authentication.getName())
                    .ifPresent(user -> model.addAttribute("currentUser", user));
        }
    }

    private UserSystem getAuthenticatedUser(Authentication authentication) {
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            throw new AccessDeniedException("É necessário estar autenticado.");
        }
        return userSystemService.buscarPorEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário autenticado não encontrado"));
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}
