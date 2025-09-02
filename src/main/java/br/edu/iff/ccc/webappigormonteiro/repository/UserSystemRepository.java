package br.edu.iff.ccc.webappigormonteiro.repository;

import br.edu.iff.ccc.webappigormonteiro.entity.UserSystem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSystemRepository extends JpaRepository<UserSystem, Long> {
    Optional<UserSystem> findByEmail(String email);
}
