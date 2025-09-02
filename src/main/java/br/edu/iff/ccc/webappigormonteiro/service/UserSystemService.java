package br.edu.iff.ccc.webappigormonteiro.service;

import br.edu.iff.ccc.webappigormonteiro.entity.UserSystem;
import br.edu.iff.ccc.webappigormonteiro.entity.UserSystem.Role;
import br.edu.iff.ccc.webappigormonteiro.entity.UserSystem.Status;
import br.edu.iff.ccc.webappigormonteiro.repository.UserSystemRepository;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Service
public class UserSystemService {

    private final UserSystemRepository repository;

    public UserSystemService(UserSystemRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    void seed() {
        if (repository.count() == 0) {
            repository.save(new UserSystem(null, "Alice", "alice@example.com", Status.ATIVO, Role.ADMIN));
            repository.save(new UserSystem(null, "Bob", "bob@example.com", Status.ATIVO, Role.USER));
            repository.save(new UserSystem(null, "Carol", "carol@example.com", Status.INATIVO, Role.GUEST));
        }
    }

    public List<UserSystem> listarTodos() { return repository.findAll(); }
    public Optional<UserSystem> buscarPorId(Long id) { return repository.findById(id); }
    public UserSystem criar(UserSystem user) { return repository.save(user); }
}
