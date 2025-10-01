package br.edu.iff.ccc.webappigormonteiro.controller.view;

import br.edu.iff.ccc.webappigormonteiro.dto.UserSystemDTO;
import br.edu.iff.ccc.webappigormonteiro.dto.UserSystemUpdateDTO;
import br.edu.iff.ccc.webappigormonteiro.entity.UserSystem;
import br.edu.iff.ccc.webappigormonteiro.exception.BusinessException;
import br.edu.iff.ccc.webappigormonteiro.exception.ResourceNotFoundException;
import br.edu.iff.ccc.webappigormonteiro.service.UserSystemService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserSystemController {

    private final UserSystemService service;

    public UserSystemController(UserSystemService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(Model model) {
        prepararListagem(model);
        return "user/list"; // templates/user/list.html
    }

    @GetMapping("/{id}")
    public String detalhar(@PathVariable Long id, Model model) {
        model.addAttribute("user", service.buscarPorId(id));
        return "user/detail"; // templates/user/detail.html
    }

    @PostMapping
    public String criar(@Valid @ModelAttribute("userForm") UserSystemDTO form,
                        BindingResult bindingResult,
                        Model model) {
        if (bindingResult.hasErrors()) {
            prepararListagem(model);
            return "user/list"; // Reexibe a lista com erros
        }
        try {
            service.criar(form);
        } catch (BusinessException ex) {
            bindingResult.rejectValue("email", "email.duplicado", ex.getMessage());
            form.setSenha("");
            prepararListagem(model);
            return "user/list";
        }
        return "redirect:/users";
    }

    @GetMapping("/{id}/edit")
    public String editar(@PathVariable Long id, Model model) {
        UserSystem user = service.buscarPorId(id);
        UserSystemUpdateDTO dto = new UserSystemUpdateDTO();
        dto.setId(user.getId());
        dto.setNome(user.getNome());
        dto.setEmail(user.getEmail());
        dto.setStatus(user.getStatus());
        dto.setRole(user.getRole());

        prepararListagem(model);
        model.addAttribute("editForm", dto);
        return "user/list";
    }

    @PostMapping("/{id}/update")
    public String atualizar(@PathVariable Long id,
                            @Valid @ModelAttribute("editForm") UserSystemUpdateDTO form,
                            BindingResult bindingResult,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        form.setId(id);
        if (bindingResult.hasErrors()) {
            prepararListagem(model);
            model.addAttribute("editForm", form);
            return "user/list";
        }
        try {
            service.atualizar(form);
            redirectAttributes.addFlashAttribute("messageSuccess", "Usuário atualizado com sucesso.");
            return "redirect:/users";
        } catch (BusinessException ex) {
            String mensagem = ex.getMessage();
            if (mensagem != null) {
                String lower = mensagem.toLowerCase();
                if (lower.contains("e-mail")) {
                    bindingResult.rejectValue("email", "email.invalido", mensagem);
                } else if (lower.contains("senha")) {
                    bindingResult.rejectValue("novaSenha", "senha.invalida", mensagem);
                } else {
                    bindingResult.reject("atualizacao.erro", mensagem);
                }
            } else {
                bindingResult.reject("atualizacao.erro", "Não foi possível atualizar o usuário.");
            }
            prepararListagem(model);
            model.addAttribute("editForm", form);
            return "user/list";
        }
    }

    @PostMapping("/{id}/delete")
    public String deletar(@PathVariable Long id,
                          RedirectAttributes redirectAttributes) {
        try {
            UserSystem alvo = service.buscarPorId(id);
            service.remover(id);
            redirectAttributes.addFlashAttribute("messageSuccess", "Usuário removido com sucesso.");
        } catch (BusinessException | ResourceNotFoundException ex) {
            redirectAttributes.addFlashAttribute("messageError", ex.getMessage());
        }
        return "redirect:/users";
    }

    private void prepararListagem(Model model) {
        model.addAttribute("users", service.listarTodos());
        model.addAttribute("roles", UserSystem.Role.values());
        if (!model.containsAttribute("userForm")) {
            model.addAttribute("userForm", new UserSystemDTO());
        }
    }
}
