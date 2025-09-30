package br.edu.iff.ccc.webappigormonteiro.controller.api;

import br.edu.iff.ccc.webappigormonteiro.dto.DesafioDTO;
import br.edu.iff.ccc.webappigormonteiro.dto.DesafioPatchDTO;
import br.edu.iff.ccc.webappigormonteiro.dto.DesafioResponseDTO;
import br.edu.iff.ccc.webappigormonteiro.entity.Categoria;
import br.edu.iff.ccc.webappigormonteiro.entity.Desafio;
import br.edu.iff.ccc.webappigormonteiro.entity.UserSystem;
import br.edu.iff.ccc.webappigormonteiro.service.CategoriaService;
import br.edu.iff.ccc.webappigormonteiro.service.DesafioService;
import br.edu.iff.ccc.webappigormonteiro.service.UserSystemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
@RequestMapping("/api/v1/desafios")
@Tag(name = "Desafios", description = "Catálogo de desafios de programação")
public class DesafioApiController {

    private final DesafioService desafioService;
    private final UserSystemService userSystemService;
    private final CategoriaService categoriaService;

    public DesafioApiController(DesafioService desafioService,
                                UserSystemService userSystemService,
                                CategoriaService categoriaService) {
        this.desafioService = desafioService;
        this.userSystemService = userSystemService;
        this.categoriaService = categoriaService;
    }

    @GetMapping
    @Operation(summary = "Listar desafios", description = "Lista desafios com filtros opcionais por dificuldade, autor e categoria",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de desafios", content = @Content(array = @ArraySchema(schema = @Schema(implementation = DesafioResponseDTO.class))))
            })
    public ResponseEntity<List<DesafioResponseDTO>> listar(
            @Parameter(description = "Filtro por dificuldade")
            @RequestParam(name = "dificuldade", required = false) String dificuldade,
            @Parameter(description = "Filtro por autor")
            @RequestParam(name = "autorId", required = false) Long autorId,
            @Parameter(description = "Filtro por categoria")
            @RequestParam(name = "categoriaId", required = false) Long categoriaId
    ) {
        List<Desafio> desafios = desafioService.buscarComFiltros(dificuldade, autorId, categoriaId);
        return ResponseEntity.ok(desafios.stream().map(this::toResponse).toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar desafio", description = "Busca um desafio pelo identificador")
    public ResponseEntity<DesafioResponseDTO> buscar(@PathVariable Long id) {
        Desafio desafio = desafioService.buscarPorIdOrThrow(id);
        return ResponseEntity.ok(toResponse(desafio));
    }

    @PostMapping
    @Operation(summary = "Cadastrar desafio", description = "Cria um novo desafio")
    @ApiResponse(responseCode = "201", description = "Desafio criado", content = @Content(schema = @Schema(implementation = DesafioResponseDTO.class)))
    public ResponseEntity<DesafioResponseDTO> criar(@Valid @RequestBody DesafioDTO dto) {
        UserSystem autor = userSystemService.buscarPorId(dto.getAutorId());
        Categoria categoria = categoriaService.buscarPorId(dto.getCategoriaId());
        Desafio desafio = desafioService.criar(dto, autor, categoria);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(desafio.getId())
                .toUri();
        return ResponseEntity.created(location).body(toResponse(desafio));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar desafio", description = "Atualiza completamente um desafio")
    public ResponseEntity<DesafioResponseDTO> atualizar(@PathVariable Long id,
                                                        @Valid @RequestBody DesafioDTO dto) {
        UserSystem autor = userSystemService.buscarPorId(dto.getAutorId());
        Categoria categoria = categoriaService.buscarPorId(dto.getCategoriaId());
        Desafio desafio = desafioService.atualizar(id, dto, autor, categoria);
        return ResponseEntity.ok(toResponse(desafio));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Atualização parcial", description = "Atualiza parcialmente os dados de um desafio")
    public ResponseEntity<DesafioResponseDTO> atualizarParcial(@PathVariable Long id,
                                                               @Valid @RequestBody DesafioPatchDTO dto) {
        UserSystem autor = dto.getAutorId() != null ? userSystemService.buscarPorId(dto.getAutorId()) : null;
        Categoria categoria = dto.getCategoriaId() != null ? categoriaService.buscarPorId(dto.getCategoriaId()) : null;
        Desafio desafio = desafioService.atualizarParcial(id, dto, autor, categoria);
        return ResponseEntity.ok(toResponse(desafio));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover desafio", description = "Remove um desafio")
    @ApiResponse(responseCode = "204", description = "Desafio removido")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        desafioService.remover(id);
        return ResponseEntity.noContent().build();
    }

    private DesafioResponseDTO toResponse(Desafio desafio) {
        UserSystem autor = desafio.getAutor();
        Categoria categoria = desafio.getCategoria();
        return new DesafioResponseDTO(
                desafio.getId(),
                desafio.getTitulo(),
                desafio.getDescricao(),
                desafio.getDificuldade(),
                desafio.getLinguagemUtilizada(),
                autor != null ? autor.getId() : null,
                autor != null ? autor.getNome() : null,
                categoria != null ? categoria.getId() : null,
                categoria != null ? categoria.getNome() : null
        );
    }
}
