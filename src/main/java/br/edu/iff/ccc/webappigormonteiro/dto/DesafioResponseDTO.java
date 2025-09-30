package br.edu.iff.ccc.webappigormonteiro.dto;

public record DesafioResponseDTO(
        Long id,
        String titulo,
        String descricao,
        String dificuldade,
        String linguagemUtilizada,
        Long autorId,
        String autorNome,
        Long categoriaId,
        String categoriaNome
) {
}
