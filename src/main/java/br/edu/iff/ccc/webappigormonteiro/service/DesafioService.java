package br.edu.iff.ccc.webappigormonteiro.service;

import br.edu.iff.ccc.webappigormonteiro.model.Desafio;
import br.edu.iff.ccc.webappigormonteiro.model.UserSystem;
import br.edu.iff.ccc.webappigormonteiro.repository.DesafioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DesafioService {

    private final DesafioRepository repository;

    public DesafioService(DesafioRepository repository) {
        this.repository = repository;
    }

    public List<Desafio> listarTodos() { return repository.findAll(); }
    public Optional<Desafio> buscarPorId(Long id) { return repository.findById(id); }

    public Desafio criar(String dificuldade, UserSystem autor, String linguagem) {
        Desafio d = new Desafio(dificuldade, autor, linguagem);
        return repository.save(d);
    }

    public void remover(Long id) { repository.deleteById(id); }
}
