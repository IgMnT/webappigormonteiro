package br.edu.iff.ccc.webappigormonteiro.controller.api;

import br.edu.iff.ccc.webappigormonteiro.dto.UserSystemDTO;
import br.edu.iff.ccc.webappigormonteiro.dto.UserSystemPatchDTO;
import br.edu.iff.ccc.webappigormonteiro.dto.UserSystemResponseDTO;
import br.edu.iff.ccc.webappigormonteiro.dto.UserSystemUpdateDTO;
import br.edu.iff.ccc.webappigormonteiro.entity.UserSystem;
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
@RequestMapping("/api/v1/users")
@Tag(name = "Usuários", description = "Gestão de contas da plataforma")
public class UserSystemApiController {

    private final UserSystemService userSystemService;

    public UserSystemApiController(UserSystemService userSystemService) {
        this.userSystemService = userSystemService;
    }

    @GetMapping
    @Operation(summary = "Listar usuários", description = "Lista usuários com filtros opcionais por status e perfil",
            responses = @ApiResponse(responseCode = "200", description = "Lista de usuários",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserSystemResponseDTO.class)))))
    public ResponseEntity<List<UserSystemResponseDTO>> listar(
            @Parameter(description = "Filtrar por status")
            @RequestParam(name = "status", required = false) UserSystem.Status status,
            @Parameter(description = "Filtrar por perfil")
            @RequestParam(name = "role", required = false) UserSystem.Role role
    ) {
        List<UserSystem> usuarios = userSystemService.buscarPorFiltro(status, role);
        return ResponseEntity.ok(usuarios.stream().map(this::toResponse).toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário", description = "Busca um usuário pelo identificador")
    public ResponseEntity<UserSystemResponseDTO> buscar(@PathVariable Long id) {
        UserSystem user = userSystemService.buscarPorId(id);
        return ResponseEntity.ok(toResponse(user));
    }

    @PostMapping
    @Operation(summary = "Criar usuário", description = "Cria um novo usuário")
    @ApiResponse(responseCode = "201", description = "Usuário criado", content = @Content(schema = @Schema(implementation = UserSystemResponseDTO.class)))
    public ResponseEntity<UserSystemResponseDTO> criar(@Valid @RequestBody UserSystemDTO dto) {
        UserSystem user = userSystemService.criar(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();
        return ResponseEntity.created(location).body(toResponse(user));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar usuário", description = "Atualiza completamente os dados de um usuário")
    public ResponseEntity<UserSystemResponseDTO> atualizar(@PathVariable Long id,
                                                           @Valid @RequestBody UserSystemUpdateDTO dto) {
        dto.setId(id);
        UserSystem atualizado = userSystemService.atualizar(dto);
        return ResponseEntity.ok(toResponse(atualizado));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Atualização parcial", description = "Atualiza parcialmente os dados de um usuário")
    public ResponseEntity<UserSystemResponseDTO> atualizarParcial(@PathVariable Long id,
                                                                  @Valid @RequestBody UserSystemPatchDTO dto) {
        UserSystem atualizado = userSystemService.atualizarParcial(id, dto);
        return ResponseEntity.ok(toResponse(atualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover usuário", description = "Remove um usuário")
    @ApiResponse(responseCode = "204", description = "Usuário removido")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        userSystemService.remover(id);
        return ResponseEntity.noContent().build();
    }

    private UserSystemResponseDTO toResponse(UserSystem user) {
        return new UserSystemResponseDTO(user.getId(), user.getNome(), user.getEmail(), user.getStatus(), user.getRole());
    }
}
