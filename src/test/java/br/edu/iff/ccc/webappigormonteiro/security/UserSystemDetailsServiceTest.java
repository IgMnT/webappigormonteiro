package br.edu.iff.ccc.webappigormonteiro.security;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserSystemDetailsServiceTest {

    @Test
    void deveLancarExcecaoAoTentarInstanciar() throws Exception {
        var constructor = UserSystemDetailsService.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        assertThatThrownBy(constructor::newInstance)
                .hasCauseInstanceOf(UnsupportedOperationException.class)
                .hasRootCauseMessage("Spring Security foi removido do projeto.");
    }
}
