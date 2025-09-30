package br.edu.iff.ccc.webappigormonteiro.service;

import br.edu.iff.ccc.webappigormonteiro.dto.DesafioDTO;
import br.edu.iff.ccc.webappigormonteiro.dto.DesafioPatchDTO;
import br.edu.iff.ccc.webappigormonteiro.entity.Categoria;
import br.edu.iff.ccc.webappigormonteiro.entity.Desafio;
import br.edu.iff.ccc.webappigormonteiro.entity.UserSystem;
import br.edu.iff.ccc.webappigormonteiro.exception.BusinessException;
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
    public List<Desafio> buscarComFiltros(String dificuldade, Long autorId, Long categoriaId) {
        if ((dificuldade == null || dificuldade.isBlank()) && autorId == null && categoriaId == null) {
            return listarTodos();
        }
        String dificuldadeFiltrada = dificuldade == null || dificuldade.isBlank() ? null : dificuldade.trim();
        return repository.search(dificuldadeFiltrada, autorId, categoriaId);
    }
    public Optional<Desafio> buscarPorId(Long id) { return repository.findById(id); }

    public Desafio buscarPorIdOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Desafio não encontrado"));
    }

    @Transactional
    public Desafio criar(DesafioDTO dto, UserSystem autor, Categoria categoria) {
        String titulo = dto.getTitulo().trim();
        repository.findByTituloIgnoreCase(titulo).ifPresent(existing -> {
            throw new BusinessException("Já existe um desafio com esse título");
        });
        Desafio desafio = new Desafio(
                titulo,
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

    @Transactional
    public Desafio atualizar(Long id, DesafioDTO dto, UserSystem autor, Categoria categoria) {
        Desafio existente = buscarPorIdOrThrow(id);
        String novoTitulo = dto.getTitulo().trim();
        repository.findByTituloIgnoreCase(novoTitulo)
                .filter(outro -> !outro.getId().equals(id))
                .ifPresent(outro -> {
                    throw new BusinessException("Já existe um desafio com esse título");
                });
        existente.setTitulo(novoTitulo);
        existente.setDescricao(dto.getDescricao().trim());
        existente.setDificuldade(dto.getDificuldade().trim());
        existente.setLinguagemUtilizada(dto.getLinguagemUtilizada().trim());
        existente.setAutor(autor);
        existente.setCategoria(categoria);
        return repository.save(existente);
    }

    @Transactional
    public Desafio atualizarParcial(Long id, DesafioPatchDTO dto, UserSystem autor, Categoria categoria) {
        Desafio existente = buscarPorIdOrThrow(id);
        if (dto.getTitulo() != null) {
            String titulo = dto.getTitulo().trim();
            if (titulo.isEmpty()) {
                throw new BusinessException("O título do desafio não pode ser vazio");
            }
            repository.findByTituloIgnoreCase(titulo)
                    .filter(outro -> !outro.getId().equals(id))
                    .ifPresent(outro -> {
                        throw new BusinessException("Já existe um desafio com esse título");
                    });
            existente.setTitulo(titulo);
        }
        if (dto.getDescricao() != null) {
            String descricao = dto.getDescricao().trim();
            existente.setDescricao(descricao);
        }
        if (dto.getDificuldade() != null) {
            String dificuldade = dto.getDificuldade().trim();
            if (dificuldade.isEmpty()) {
                throw new BusinessException("A dificuldade não pode ser vazia");
            }
            existente.setDificuldade(dificuldade);
        }
        if (dto.getLinguagemUtilizada() != null) {
            String linguagem = dto.getLinguagemUtilizada().trim();
            existente.setLinguagemUtilizada(linguagem);
        }
        if (autor != null) {
            existente.setAutor(autor);
        }
        if (categoria != null) {
            existente.setCategoria(categoria);
        }
        return repository.save(existente);
    }
}
