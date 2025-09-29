package br.edu.iff.ccc.webappigormonteiro.service;

import br.edu.iff.ccc.webappigormonteiro.dto.UserSystemDTO;
import br.edu.iff.ccc.webappigormonteiro.dto.UserSystemUpdateDTO;
import br.edu.iff.ccc.webappigormonteiro.entity.UserSystem;
import br.edu.iff.ccc.webappigormonteiro.entity.UserSystem.Role;
import br.edu.iff.ccc.webappigormonteiro.entity.UserSystem.Status;
import br.edu.iff.ccc.webappigormonteiro.exception.BusinessException;
import br.edu.iff.ccc.webappigormonteiro.exception.ResourceNotFoundException;
import br.edu.iff.ccc.webappigormonteiro.repository.UserSystemRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserSystemService {

    private final UserSystemRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserSystemService(UserSystemRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
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
            UserSystem novo = new UserSystem(null, nome, emailNormalizado, status, role, passwordEncoder.encode(senha));
            repository.save(novo);
        });
    }

    public List<UserSystem> listarTodos() {
        return repository.findAll();
    }

    public List<UserSystem> listarAtivos() {
        return repository.findByStatus(Status.ATIVO);
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
        passwordEncoder.encode(dto.getSenha().trim())
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

        boolean unicoAdmin = user.getRole() == Role.ADMIN && repository.countByRole(Role.ADMIN) <= 1;
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
            String trimmed = dto.getNovaSenha().trim();
            if (trimmed.length() < 8) {
                throw new BusinessException("A nova senha deve ter pelo menos 8 caracteres.");
            }
            user.setPasswordHash(passwordEncoder.encode(trimmed));
        }

        return repository.save(user);
    }
}
