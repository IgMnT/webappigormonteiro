package br.edu.iff.ccc.webappigormonteiro.repository;

import br.edu.iff.ccc.webappigormonteiro.entity.UserSystem;
import br.edu.iff.ccc.webappigormonteiro.entity.UserSystem.Status;
import br.edu.iff.ccc.webappigormonteiro.entity.UserSystem.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserSystemRepository extends JpaRepository<UserSystem, Long> {
    Optional<UserSystem> findByEmail(String email);
    List<UserSystem> findByStatus(Status status);
    long countByRole(Role role);
}
