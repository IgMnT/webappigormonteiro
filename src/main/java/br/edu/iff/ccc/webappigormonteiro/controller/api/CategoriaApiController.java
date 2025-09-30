package br.edu.iff.ccc.webappigormonteiro.controller.api;

import br.edu.iff.ccc.webappigormonteiro.dto.CategoriaDTO;
import br.edu.iff.ccc.webappigormonteiro.dto.CategoriaPatchDTO;
import br.edu.iff.ccc.webappigormonteiro.dto.CategoriaResponseDTO;
import br.edu.iff.ccc.webappigormonteiro.entity.Categoria;
import br.edu.iff.ccc.webappigormonteiro.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/categorias")
@Tag(name = "Categorias", description = "Gerenciamento de categorias de desafios")
public class CategoriaApiController {

    private final CategoriaService categoriaService;

    public CategoriaApiController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    @Operation(summary = "Listar categorias", description = "Lista categorias com filtro opcional por nome")
    public ResponseEntity<List<CategoriaResponseDTO>> listar(
            @Parameter(description = "Filtro por nome da categoria")
            @RequestParam(name = "nome", required = false) String nome
    ) {
        List<Categoria> categorias = categoriaService.buscarPorNome(nome);
        return ResponseEntity.ok(categorias.stream().map(this::toResponse).toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar categoria", description = "Recupera uma categoria pelo identificador")
    public ResponseEntity<CategoriaResponseDTO> buscar(@PathVariable Long id) {
        Categoria categoria = categoriaService.buscarPorId(id);
        return ResponseEntity.ok(toResponse(categoria));
    }

    @PostMapping
    @Operation(summary = "Cadastrar categoria", description = "Cria uma nova categoria")
    @ApiResponse(responseCode = "201", description = "Categoria criada", content = @Content(schema = @Schema(implementation = CategoriaResponseDTO.class)))
    public ResponseEntity<CategoriaResponseDTO> criar(@Valid @RequestBody CategoriaDTO dto) {
        Categoria categoria = categoriaService.criar(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(categoria.getId())
                .toUri();
        return ResponseEntity.created(location).body(toResponse(categoria));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar categoria", description = "Atualiza todos os dados de uma categoria")
    public ResponseEntity<CategoriaResponseDTO> atualizar(@PathVariable Long id,
                                                          @Valid @RequestBody CategoriaDTO dto) {
        Categoria categoria = categoriaService.atualizar(id, dto);
        return ResponseEntity.ok(toResponse(categoria));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Atualização parcial", description = "Atualiza parcialmente os dados de uma categoria")
    public ResponseEntity<CategoriaResponseDTO> atualizarParcial(@PathVariable Long id,
                                                                 @Valid @RequestBody CategoriaPatchDTO dto) {
        Categoria categoria = categoriaService.atualizarParcial(id, dto);
        return ResponseEntity.ok(toResponse(categoria));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir categoria", description = "Remove uma categoria")
    @ApiResponse(responseCode = "204", description = "Categoria removida")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        categoriaService.remover(id);
        return ResponseEntity.noContent().build();
    }

    private CategoriaResponseDTO toResponse(Categoria categoria) {
        return new CategoriaResponseDTO(categoria.getId(), categoria.getNome(), categoria.getDescricao());
    }
}
