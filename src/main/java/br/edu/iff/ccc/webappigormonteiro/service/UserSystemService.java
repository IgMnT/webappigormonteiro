package br.edu.iff.ccc.webappigormonteiro.service;

import br.edu.iff.ccc.webappigormonteiro.model.UserSystem;
import br.edu.iff.ccc.webappigormonteiro.model.UserSystem.Role;
import br.edu.iff.ccc.webappigormonteiro.model.UserSystem.Status;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserSystemService {

    private final List<UserSystem> fakeUsers = Arrays.asList(
            new UserSystem(1L, "Alice", "alice@example.com", Status.ATIVO, Role.ADMIN),
            new UserSystem(2L, "Bob", "bob@example.com", Status.ATIVO, Role.USER),
            new UserSystem(3L, "Carol", "carol@example.com", Status.INATIVO, Role.GUEST)
    );

    public List<UserSystem> listarTodos() {
        return fakeUsers;
    }

    public Optional<UserSystem> buscarPorId(Long id) {
        return fakeUsers.stream().filter(u -> u.getId().equals(id)).findFirst();
    }
}
