package br.edu.iff.ccc.webappigormonteiro.service;

import br.edu.iff.ccc.webappigormonteiro.dto.DesafioDTO;
import br.edu.iff.ccc.webappigormonteiro.entity.Categoria;
import br.edu.iff.ccc.webappigormonteiro.entity.Desafio;
import br.edu.iff.ccc.webappigormonteiro.entity.UserSystem;
import br.edu.iff.ccc.webappigormonteiro.exception.ResourceNotFoundException;
import br.edu.iff.ccc.webappigormonteiro.repository.DesafioRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class DesafioService {

    private final DesafioRepository repository;

    public DesafioService(DesafioRepository repository) {
        this.repository = repository;
    }

    public List<Desafio> listarTodos() { return repository.findAll(Sort.by("titulo").ascending()); }
    public Optional<Desafio> buscarPorId(Long id) { return repository.findById(id); }

    public Desafio buscarPorIdOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Desafio não encontrado"));
    }

    @Transactional
    public Desafio criar(DesafioDTO dto, UserSystem autor, Categoria categoria) {
        Desafio desafio = new Desafio(
                dto.getTitulo().trim(),
                dto.getDescricao().trim(),
        dto.getDificuldade().trim(),
                autor,
                categoria,
                dto.getLinguagemUtilizada().trim()
        );
        return repository.save(desafio);
    }

    @Transactional
    public void remover(Long id) {
        Desafio desafio = buscarPorIdOrThrow(id);
        repository.delete(desafio);
    }
}
