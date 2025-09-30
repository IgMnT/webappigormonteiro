package br.edu.iff.ccc.webappigormonteiro.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Size;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DesafioPatchDTO {

    @Size(max = 120)
    private String titulo;

    @Size(max = 500)
    private String descricao;

    @Size(max = 40)
    private String dificuldade;

    @Size(max = 60)
    private String linguagemUtilizada;

    private Long autorId;

    private Long categoriaId;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDificuldade() {
        return dificuldade;
    }

    public void setDificuldade(String dificuldade) {
        this.dificuldade = dificuldade;
    }

    public String getLinguagemUtilizada() {
        return linguagemUtilizada;
    }

    public void setLinguagemUtilizada(String linguagemUtilizada) {
        this.linguagemUtilizada = linguagemUtilizada;
    }

    public Long getAutorId() {
        return autorId;
    }

    public void setAutorId(Long autorId) {
        this.autorId = autorId;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }
}
