package br.edu.iff.ccc.webappigormonteiro.service;

import br.edu.iff.ccc.webappigormonteiro.dto.UserSystemDTO;
import br.edu.iff.ccc.webappigormonteiro.dto.UserSystemPatchDTO;
import br.edu.iff.ccc.webappigormonteiro.dto.UserSystemUpdateDTO;
import br.edu.iff.ccc.webappigormonteiro.entity.UserSystem;
import br.edu.iff.ccc.webappigormonteiro.entity.UserSystem.Role;
import br.edu.iff.ccc.webappigormonteiro.entity.UserSystem.Status;
import br.edu.iff.ccc.webappigormonteiro.exception.BusinessException;
import br.edu.iff.ccc.webappigormonteiro.exception.ResourceNotFoundException;
import br.edu.iff.ccc.webappigormonteiro.repository.UserSystemRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserSystemService {

    private final UserSystemRepository repository;

    public UserSystemService(UserSystemRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    void seed() {
        if (repository.count() == 0) {
            criarUsuarioSeed("Igor Admin", "igor@example.com", Status.ATIVO, Role.ADMIN, "admin123");
            criarUsuarioSeed("Bruno Autor", "bruno@example.com", Status.ATIVO, Role.AUTHOR, "autor123");
            criarUsuarioSeed("Carla Visitante", "carla@example.com", Status.ATIVO, Role.VISITOR, "visitante123");
        }
    }

    private void criarUsuarioSeed(String nome, String email, Status status, Role role, String senha) {
        String emailNormalizado = email.trim().toLowerCase();
        repository.findByEmail(emailNormalizado).ifPresentOrElse(user -> {}, () -> {
            UserSystem novo = new UserSystem(null, nome, emailNormalizado, status, role, sanitizeSenha(senha));
            repository.save(novo);
        });
    }

    public List<UserSystem> listarTodos() {
        return repository.findAll();
    }

    public List<UserSystem> listarAtivos() {
        return repository.findByStatus(Status.ATIVO);
    }

    public List<UserSystem> buscarPorFiltro(Status status, Role role) {
        if (status != null && role != null) {
            return repository.findByStatusAndRole(status, role);
        }
        if (status != null) {
            return repository.findByStatus(status);
        }
        if (role != null) {
            return repository.findByRole(role);
        }
        return listarTodos();
    }

    public List<UserSystem> listarPossiveisAutores() {
        return listarAtivos().stream()
                .filter(user -> user.getRole() == Role.ADMIN || user.getRole() == Role.AUTHOR)
                .toList();
    }

    public UserSystem buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }

    public Optional<UserSystem> buscarPorEmail(String email) {
        if (email == null) {
            return Optional.empty();
        }
        return repository.findByEmail(email.trim().toLowerCase());
    }

    @Transactional
    public UserSystem criar(UserSystemDTO dto) {
        String emailNormalizado = dto.getEmail().trim().toLowerCase();
        repository.findByEmail(emailNormalizado).ifPresent(existing -> {
            throw new BusinessException("Já existe um usuário com esse e-mail");
        });
        UserSystem novo = new UserSystem(
                null,
                dto.getNome().trim(),
                emailNormalizado,
                dto.getStatus(),
                dto.getRole(),
                sanitizeSenha(dto.getSenha())
        );
        return repository.save(novo);
    }

    @Transactional
    public void remover(Long id) {
        UserSystem user = buscarPorId(id);
        if (user.getRole() == Role.ADMIN && repository.countByRole(Role.ADMIN) <= 1) {
            throw new BusinessException("Não é possível remover o único administrador do sistema.");
        }
        repository.delete(user);
    }

    @Transactional
    public UserSystem atualizar(UserSystemUpdateDTO dto) {
        UserSystem user = buscarPorId(dto.getId());

        String emailNormalizado = dto.getEmail().trim().toLowerCase();
        if (!emailNormalizado.equalsIgnoreCase(user.getEmail())) {
            repository.findByEmail(emailNormalizado).ifPresent(existing -> {
                throw new BusinessException("Já existe um usuário com esse e-mail");
            });
        }

        boolean unicoAdmin = isUnicoAdmin(user);
        if (unicoAdmin && dto.getRole() != Role.ADMIN) {
            throw new BusinessException("Não é possível alterar o perfil do único administrador.");
        }
        if (unicoAdmin && dto.getStatus() != Status.ATIVO) {
            throw new BusinessException("Não é possível inativar o único administrador.");
        }

        user.setNome(dto.getNome().trim());
        user.setEmail(emailNormalizado);
        user.setStatus(dto.getStatus());
        user.setRole(dto.getRole());

        if (dto.getNovaSenha() != null && !dto.getNovaSenha().isBlank()) {
            user.setPasswordHash(sanitizeSenha(dto.getNovaSenha()));
        }

        return repository.save(user);
    }

    @Transactional
    public UserSystem atualizarParcial(Long id, UserSystemPatchDTO dto) {
        UserSystem user = buscarPorId(id);

        if (dto.getEmail() != null) {
            String emailNormalizado = dto.getEmail().trim().toLowerCase();
            if (!emailNormalizado.equalsIgnoreCase(user.getEmail())) {
                repository.findByEmail(emailNormalizado).ifPresent(existing -> {
                    throw new BusinessException("Já existe um usuário com esse e-mail");
                });
                user.setEmail(emailNormalizado);
            }
        }

        if (dto.getNome() != null) {
            String nome = dto.getNome().trim();
            if (nome.isEmpty()) {
                throw new BusinessException("O nome do usuário não pode ser vazio");
            }
            user.setNome(nome);
        }

        if (dto.getStatus() != null) {
            if (isUnicoAdmin(user) && dto.getStatus() != Status.ATIVO) {
                throw new BusinessException("Não é possível inativar o único administrador.");
            }
            user.setStatus(dto.getStatus());
        }

        if (dto.getRole() != null) {
            if (isUnicoAdmin(user) && dto.getRole() != Role.ADMIN) {
                throw new BusinessException("Não é possível alterar o perfil do único administrador.");
            }
            user.setRole(dto.getRole());
        }

        if (dto.getNovaSenha() != null && !dto.getNovaSenha().isBlank()) {
            user.setPasswordHash(sanitizeSenha(dto.getNovaSenha()));
        }

        return repository.save(user);
    }

    private boolean isUnicoAdmin(UserSystem user) {
        return user.getRole() == Role.ADMIN && repository.countByRole(Role.ADMIN) <= 1;
    }

    private String sanitizeSenha(String senha) {
        if (senha == null) {
            throw new BusinessException("A senha é obrigatória.");
        }
        String trimmed = senha.trim();
        if (trimmed.length() < 8) {
            throw new BusinessException("A senha deve ter pelo menos 8 caracteres.");
        }
        return trimmed;
    }
}
