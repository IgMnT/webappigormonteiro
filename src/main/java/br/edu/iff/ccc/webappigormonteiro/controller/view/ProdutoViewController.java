package br.edu.iff.ccc.webappigormonteiro.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Controller
@RequestMapping("/produto")
public class ProdutoViewController {

    @GetMapping("/{id}")
    public String detalharProduto(@PathVariable("id") Long id, Model model) {
        // Mock de produto (sem persistência ainda)
        Produto produto = new Produto(id,
                "Desafio " + id,
                "Exemplo de problema de programação para a plataforma",
                new BigDecimal("99.90")
        );
        model.addAttribute("produto", produto);
        return "produto-detalhe.html"; // Template Thymeleaf para exibir os dados
    }

    @PostMapping
    public String criarProduto(@RequestParam("NOME") String nome,
                               @RequestParam("DESCRICAO") String descricao,
                               @RequestParam("PRECO") BigDecimal preco) {
        System.out.println("=== Novo Produto Recebido ===");
        System.out.println("Nome: " + nome);
        System.out.println("Descrição: " + descricao);
        System.out.println("Preço: " + preco);
        System.out.println("=============================");
        // Redireciona para a home após o POST
        return "redirect:/principal";
    }

    // POJO simples para a View
    public static class Produto {
        private Long id;
        private String nome;
        private String descricao;
        private BigDecimal preco;

        public Produto(Long id, String nome, String descricao, BigDecimal preco) {
            this.id = id;
            this.nome = nome;
            this.descricao = descricao;
            this.preco = preco;
        }

        public Long getId() { return id; }
        public String getNome() { return nome; }
        public String getDescricao() { return descricao; }
        public BigDecimal getPreco() { return preco; }
    }
}
