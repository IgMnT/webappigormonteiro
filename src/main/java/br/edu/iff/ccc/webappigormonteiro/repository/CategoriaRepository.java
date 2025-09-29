package br.edu.iff.ccc.webappigormonteiro.repository;

import br.edu.iff.ccc.webappigormonteiro.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findByNomeIgnoreCase(String nome);
}
