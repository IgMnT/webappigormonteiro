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
    @Size(max = 120)
    @Column(nullable = false, length = 120)
    private String titulo;

    @NotBlank
    @Size(max = 500)
    @Column(nullable = false, length = 500)
    private String descricao;

    @NotBlank
    @Size(max = 40)
    @Column(nullable = false, length = 40)
    private String dificuldade;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "autor_id", nullable = false)
    private UserSystem autor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @NotBlank
    @Size(max = 60)
    @Column(name = "linguagem_utilizada", nullable = false, length = 60)
    private String linguagemUtilizada;

    public Desafio() {}

    public Desafio(String titulo, String descricao, String dificuldade, UserSystem autor, Categoria categoria, String linguagemUtilizada) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.dificuldade = dificuldade;
        this.autor = autor;
        this.categoria = categoria;
        this.linguagemUtilizada = linguagemUtilizada;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getDificuldade() { return dificuldade; }
    public void setDificuldade(String dificuldade) { this.dificuldade = dificuldade; }

    public UserSystem getAutor() { return autor; }
    public void setAutor(UserSystem autor) { this.autor = autor; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public String getLinguagemUtilizada() { return linguagemUtilizada; }
    public void setLinguagemUtilizada(String linguagemUtilizada) { this.linguagemUtilizada = linguagemUtilizada; }
}
