package br.edu.iff.ccc.webappigormonteiro.dto;

import br.edu.iff.ccc.webappigormonteiro.entity.UserSystem;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserSystemUpdateDTO {

    private Long id;

    @NotBlank
    @Size(max = 100)
    private String nome;

    @Email
    @NotBlank
    @Size(max = 120)
    private String email;

    @NotNull
    private UserSystem.Status status;

    @NotNull
    private UserSystem.Role role;

    private String novaSenha;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
