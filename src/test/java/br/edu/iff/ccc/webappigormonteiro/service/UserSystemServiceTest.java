package br.edu.iff.ccc.webappigormonteiro.service;

import br.edu.iff.ccc.webappigormonteiro.dto.UserSystemUpdateDTO;
import br.edu.iff.ccc.webappigormonteiro.entity.UserSystem;
import br.edu.iff.ccc.webappigormonteiro.entity.UserSystem.Role;
import br.edu.iff.ccc.webappigormonteiro.entity.UserSystem.Status;
import br.edu.iff.ccc.webappigormonteiro.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class UserSystemServiceTest {

    @Autowired
    private UserSystemService userSystemService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void deveAtualizarUsuarioComSucesso() {
        UserSystem bruno = userSystemService.buscarPorEmail("bruno@example.com").orElseThrow();

        UserSystemUpdateDTO dto = dtoFrom(bruno);
        dto.setNome("Bruno Atualizado");
        dto.setEmail("bruno.atualizado@example.com");
        dto.setStatus(Status.ATIVO);
        dto.setRole(Role.AUTHOR);
        dto.setNovaSenha("novaSenha123");

        UserSystem atualizado = userSystemService.atualizar(dto);

        assertThat(atualizado.getNome()).isEqualTo("Bruno Atualizado");
        assertThat(atualizado.getEmail()).isEqualTo("bruno.atualizado@example.com");
        assertThat(passwordEncoder.matches("novaSenha123", atualizado.getPasswordHash())).isTrue();
    }

    @Test
    void naoDevePermitirEmailDuplicado() {
        UserSystem bruno = userSystemService.buscarPorEmail("bruno@example.com").orElseThrow();

        UserSystemUpdateDTO dto = dtoFrom(bruno);
    dto.setEmail("igor@example.com");

        assertThatThrownBy(() -> userSystemService.atualizar(dto))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Já existe um usuário com esse e-mail");
    }

    @Test
    void naoDevePermitirTrocarPerfilDoUnicoAdmin() {
    UserSystem igor = userSystemService.buscarPorEmail("igor@example.com").orElseThrow();

    UserSystemUpdateDTO dto = dtoFrom(igor);
        dto.setRole(Role.AUTHOR);

        assertThatThrownBy(() -> userSystemService.atualizar(dto))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("único administrador");
    }

    @Test
    void naoDeveInativarUnicoAdmin() {
    UserSystem igor = userSystemService.buscarPorEmail("igor@example.com").orElseThrow();

    UserSystemUpdateDTO dto = dtoFrom(igor);
        dto.setStatus(Status.INATIVO);

        assertThatThrownBy(() -> userSystemService.atualizar(dto))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("único administrador");
    }

    private UserSystemUpdateDTO dtoFrom(UserSystem user) {
        UserSystemUpdateDTO dto = new UserSystemUpdateDTO();
        dto.setId(user.getId());
        dto.setNome(user.getNome());
        dto.setEmail(user.getEmail());
        dto.setStatus(user.getStatus());
        dto.setRole(user.getRole());
        dto.setNovaSenha(null);
        return dto;
    }
}
