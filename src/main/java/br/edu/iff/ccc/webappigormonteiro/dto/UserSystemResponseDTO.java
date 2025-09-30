package br.edu.iff.ccc.webappigormonteiro.dto;

import br.edu.iff.ccc.webappigormonteiro.entity.UserSystem;

public record UserSystemResponseDTO(
        Long id,
        String nome,
        String email,
        UserSystem.Status status,
        UserSystem.Role role
) {
}
