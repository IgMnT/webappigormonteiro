package br.edu.iff.ccc.webappigormonteiro.service;

import br.edu.iff.ccc.webappigormonteiro.dto.CategoriaDTO;
import br.edu.iff.ccc.webappigormonteiro.entity.Categoria;
import br.edu.iff.ccc.webappigormonteiro.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class CategoriaServiceTest {

    @Autowired
    private CategoriaService categoriaService;

    @Test
    void deveCriarCategoriaComSucesso() {
        CategoriaDTO dto = new CategoriaDTO();
        dto.setNome("Testes Automatizados" + System.nanoTime());
        dto.setDescricao("Categoria criada durante os testes automatizados");

        Categoria categoria = categoriaService.criar(dto);

        assertThat(categoria.getId()).isNotNull();
        assertThat(categoria.getNome()).startsWith("Testes Automatizados");
    }

    @Test
    void naoDevePermitirNomeDuplicado() {
        CategoriaDTO dto = new CategoriaDTO();
        dto.setNome("Algoritmos");
        dto.setDescricao("duplicado");

        assertThatThrownBy(() -> categoriaService.criar(dto))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Já existe uma categoria cadastrada");
    }
}
