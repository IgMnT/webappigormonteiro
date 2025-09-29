package br.edu.iff.ccc.webappigormonteiro.service;

import br.edu.iff.ccc.webappigormonteiro.dto.CategoriaDTO;
import br.edu.iff.ccc.webappigormonteiro.entity.Categoria;
import br.edu.iff.ccc.webappigormonteiro.exception.BusinessException;
import br.edu.iff.ccc.webappigormonteiro.exception.ResourceNotFoundException;
import br.edu.iff.ccc.webappigormonteiro.repository.CategoriaRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CategoriaService {

    private final CategoriaRepository repository;

    public CategoriaService(CategoriaRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    @Transactional
    void seed() {
        if (repository.count() == 0) {
            repository.save(new Categoria(null, "Algoritmos", "Desafios focados em lógica e estruturas de dados."));
            repository.save(new Categoria(null, "Desenvolvimento Web", "Desafios com foco em frontend e backend."));
            repository.save(new Categoria(null, "Banco de Dados", "Consultas SQL e modelagem."));
        }
    }

    public List<Categoria> listarTodas() {
        return repository.findAll(Sort.by("nome").ascending());
    }

    public Categoria buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));
    }

    @Transactional
    public Categoria criar(CategoriaDTO dto) {
        repository.findByNomeIgnoreCase(dto.getNome()).ifPresent(existing -> {
            throw new BusinessException("Já existe uma categoria cadastrada com esse nome");
        });
        String descricao = dto.getDescricao() == null ? null : dto.getDescricao().trim();
        Categoria categoria = new Categoria(null, dto.getNome().trim(), descricao);
        return repository.save(categoria);
    }
}
