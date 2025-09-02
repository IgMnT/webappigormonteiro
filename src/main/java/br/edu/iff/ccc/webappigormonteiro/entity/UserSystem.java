package br.edu.iff.ccc.webappigormonteiro.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "user_system")
public class UserSystem {

    public enum Status { ATIVO, INATIVO }
    public enum Role { ADMIN, USER, GUEST }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String nome;

    @Email
    @NotBlank
    @Size(max = 120)
    @Column(nullable = false, unique = true, length = 120)
    private String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Desafio> desafios = new ArrayList<>();

    public UserSystem() {}

    public UserSystem(Long id, String nome, String email, Status status, Role role) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.status = status;
        this.role = role;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public List<Desafio> getDesafios() { return desafios; }
    public void addDesafio(Desafio d) {
        desafios.add(d);
        d.setAutor(this);
    }
    public void removeDesafio(Desafio d) {
        desafios.remove(d);
        d.setAutor(null);
    }
}
