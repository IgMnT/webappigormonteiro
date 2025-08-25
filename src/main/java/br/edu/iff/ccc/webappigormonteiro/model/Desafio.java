package br.edu.iff.ccc.webappigormonteiro.model;

import jakarta.persistence.*; 
import java.io.Serializable;

@Entity
@Table(name = "desafio")
public class Desafio implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String dificuldade;
    @ManyToOne(fetch = FetchType.EAGER) 
    @JoinColumn(name = "autor_id")
    private UserSystem autor; 
    private String linguagemUtilizada;

    public Desafio() {}

    public Desafio(String dificuldade, UserSystem autor, String linguagemUtilizada) {
        this.dificuldade = dificuldade;
        this.autor = autor;
        this.linguagemUtilizada = linguagemUtilizada;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDificuldade() { return dificuldade; }
    public void setDificuldade(String dificuldade) { this.dificuldade = dificuldade; }

    public UserSystem getAutor() { return autor; }
    public void setAutor(UserSystem autor) { this.autor = autor; }

    public String getLinguagemUtilizada() { return linguagemUtilizada; }
    public void setLinguagemUtilizada(String linguagemUtilizada) { this.linguagemUtilizada = linguagemUtilizada; }
}
