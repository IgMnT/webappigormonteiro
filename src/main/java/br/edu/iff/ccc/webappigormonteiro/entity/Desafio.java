package br.edu.iff.ccc.webappigormonteiro.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "desafio")
public class Desafio implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 40)
    @Column(nullable = false, length = 40)
    private String dificuldade;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "autor_id", nullable = false)
    private UserSystem autor;

    @NotBlank
    @Size(max = 60)
    @Column(name = "linguagem_utilizada", nullable = false, length = 60)
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
