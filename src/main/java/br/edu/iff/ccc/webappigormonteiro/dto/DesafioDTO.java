package br.edu.iff.ccc.webappigormonteiro.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class DesafioDTO {

    @NotBlank
    @Size(max = 120)
    private String titulo;

    @NotBlank
    @Size(max = 500)
    private String descricao;

    @NotBlank
    @Size(max = 40)
    private String dificuldade;

    @NotNull
    private Long autorId;

    @NotBlank
    @Size(max = 60)
    private String linguagemUtilizada;

    @NotNull
    private Long categoriaId;

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getDificuldade() { return dificuldade; }
    public void setDificuldade(String dificuldade) { this.dificuldade = dificuldade; }

    public Long getAutorId() { return autorId; }
    public void setAutorId(Long autorId) { this.autorId = autorId; }

    public String getLinguagemUtilizada() { return linguagemUtilizada; }
    public void setLinguagemUtilizada(String linguagemUtilizada) { this.linguagemUtilizada = linguagemUtilizada; }

    public Long getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Long categoriaId) { this.categoriaId = categoriaId; }
}
