package br.edu.iff.ccc.webappigormonteiro.service;

import br.edu.iff.ccc.webappigormonteiro.model.UserSystem;
import br.edu.iff.ccc.webappigormonteiro.model.UserSystem.Role;
import br.edu.iff.ccc.webappigormonteiro.model.UserSystem.Status;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserSystemService {

    private final List<UserSystem> users = new CopyOnWriteArrayList<>();
    private final AtomicLong sequence = new AtomicLong(0);

    public UserSystemService() {
        // Seed inicial
        criar(new UserSystem(null, "Alice", "alice@example.com", Status.ATIVO, Role.ADMIN));
        criar(new UserSystem(null, "Bob", "bob@example.com", Status.ATIVO, Role.USER));
        criar(new UserSystem(null, "Carol", "carol@example.com", Status.INATIVO, Role.GUEST));
    }

    public List<UserSystem> listarTodos() { return users; }

    public Optional<UserSystem> buscarPorId(Long id) {
        return users.stream().filter(u -> u.getId().equals(id)).findFirst();
    }

    public UserSystem criar(UserSystem user) {
        user.setId(sequence.incrementAndGet());
        users.add(user);
        return user;
    }
}
