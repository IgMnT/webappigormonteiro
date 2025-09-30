package br.edu.iff.ccc.webappigormonteiro.service;

import br.edu.iff.ccc.webappigormonteiro.dto.CategoriaDTO;
import br.edu.iff.ccc.webappigormonteiro.dto.CategoriaPatchDTO;
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

    public List<Categoria> buscarPorNome(String termo) {
        if (termo == null || termo.isBlank()) {
            return listarTodas();
        }
        return repository.findByNomeContainingIgnoreCase(termo.trim());
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

    @Transactional
    public Categoria atualizar(Long id, CategoriaDTO dto) {
        Categoria existente = buscarPorId(id);
        String novoNome = dto.getNome().trim();
        repository.findByNomeIgnoreCase(novoNome)
                .filter(outra -> !outra.getId().equals(id))
                .ifPresent(outra -> {
                    throw new BusinessException("Já existe uma categoria cadastrada com esse nome");
                });
        existente.setNome(novoNome);
        existente.setDescricao(dto.getDescricao() == null ? null : dto.getDescricao().trim());
        return repository.save(existente);
    }

    @Transactional
    public Categoria atualizarParcial(Long id, CategoriaPatchDTO dto) {
        Categoria existente = buscarPorId(id);
        if (dto.getNome() != null) {
            String novoNome = dto.getNome().trim();
            if (novoNome.isEmpty()) {
                throw new BusinessException("O nome da categoria não pode ser vazio");
            }
            repository.findByNomeIgnoreCase(novoNome)
                    .filter(outra -> !outra.getId().equals(id))
                    .ifPresent(outra -> {
                        throw new BusinessException("Já existe uma categoria cadastrada com esse nome");
                    });
            existente.setNome(novoNome);
        }
        if (dto.getDescricao() != null) {
            String descricao = dto.getDescricao().trim();
            existente.setDescricao(descricao.isEmpty() ? null : descricao);
        }
        return repository.save(existente);
    }

    @Transactional
    public void remover(Long id) {
        Categoria categoria = buscarPorId(id);
        if (!categoria.getDesafios().isEmpty()) {
            throw new BusinessException("Não é possível remover uma categoria com desafios associados");
        }
        repository.delete(categoria);
    }
}
