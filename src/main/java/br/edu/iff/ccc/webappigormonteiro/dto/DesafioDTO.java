package br.edu.iff.ccc.webappigormonteiro.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class DesafioDTO {

    @NotBlank
    @Size(max = 40)
    private String dificuldade;

    @NotNull
    private Long autorId;

    @NotBlank
    @Size(max = 60)
    private String linguagemUtilizada;

    public String getDificuldade() { return dificuldade; }
    public void setDificuldade(String dificuldade) { this.dificuldade = dificuldade; }

    public Long getAutorId() { return autorId; }
    public void setAutorId(Long autorId) { this.autorId = autorId; }

    public String getLinguagemUtilizada() { return linguagemUtilizada; }
    public void setLinguagemUtilizada(String linguagemUtilizada) { this.linguagemUtilizada = linguagemUtilizada; }
}
