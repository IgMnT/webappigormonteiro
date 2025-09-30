package br.edu.iff.ccc.webappigormonteiro.dto;

import br.edu.iff.ccc.webappigormonteiro.entity.UserSystem;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserSystemPatchDTO {

    @Size(max = 100)
    private String nome;

    @Email
    @Size(max = 120)
    private String email;

    private UserSystem.Status status;

    private UserSystem.Role role;

    @Size(min = 8, max = 72)
    private String novaSenha;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserSystem.Status getStatus() {
        return status;
    }

    public void setStatus(UserSystem.Status status) {
        this.status = status;
    }

    public UserSystem.Role getRole() {
        return role;
    }

    public void setRole(UserSystem.Role role) {
        this.role = role;
    }

    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }
}
