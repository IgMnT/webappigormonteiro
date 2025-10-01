package br.edu.iff.ccc.webappigormonteiro.security;

/**
 * A aplicação não utiliza mais Spring Security. A classe é mantida apenas para
 * evitar referências quebradas e sinalizar claramente que o módulo de segurança
 * foi removido.
 */
@Deprecated
public final class UserSystemDetailsService {

    private UserSystemDetailsService() {
        throw new UnsupportedOperationException("Spring Security foi removido do projeto.");
    }
}
