package br.edu.iff.ccc.webappigormonteiro.security;

import br.edu.iff.ccc.webappigormonteiro.entity.UserSystem;
import br.edu.iff.ccc.webappigormonteiro.entity.UserSystem.Role;
import br.edu.iff.ccc.webappigormonteiro.entity.UserSystem.Status;
import br.edu.iff.ccc.webappigormonteiro.repository.UserSystemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class UserSystemDetailsServiceTest {

    @Autowired
    private UserSystemDetailsService detailsService;

    @Autowired
    private UserSystemRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void deveCarregarUsuarioAtivo() {
        UserDetails userDetails = detailsService.loadUserByUsername("igor@example.com");

        assertThat(userDetails.getUsername()).isEqualTo("igor@example.com");
        assertThat(userDetails.getAuthorities())
                .extracting(grantedAuthority -> grantedAuthority.getAuthority())
                .contains("ROLE_ADMIN");
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoExiste() {
        assertThatThrownBy(() -> detailsService.loadUserByUsername("inexistente@example.com"))
                .isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    void deveRecusarUsuarioInativo() {
        UserSystem inativo = new UserSystem(null, "Inativo", "inativo@example.com",
                Status.INATIVO, Role.AUTHOR, passwordEncoder.encode("12345678"));
        userRepository.save(inativo);

        assertThatThrownBy(() -> detailsService.loadUserByUsername("inativo@example.com"))
                .isInstanceOf(DisabledException.class);
    }
}
