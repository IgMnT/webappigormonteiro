package br.edu.iff.ccc.webappigormonteiro.repository;

import br.edu.iff.ccc.webappigormonteiro.entity.Desafio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DesafioRepository extends JpaRepository<Desafio, Long> {

    @Query("""
	    select d from Desafio d
	    where (:dificuldade is null or lower(d.dificuldade) = lower(:dificuldade))
	      and (:autorId is null or d.autor.id = :autorId)
	      and (:categoriaId is null or d.categoria.id = :categoriaId)
	    order by lower(d.titulo)
	    """)
    List<Desafio> search(
	    @Param("dificuldade") String dificuldade,
	    @Param("autorId") Long autorId,
	    @Param("categoriaId") Long categoriaId
    );

    Optional<Desafio> findByTituloIgnoreCase(String titulo);
}
