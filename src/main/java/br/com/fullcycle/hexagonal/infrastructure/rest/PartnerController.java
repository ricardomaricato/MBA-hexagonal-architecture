package br.com.fullcycle.hexagonal.infrastructure.rest;

import br.com.fullcycle.hexagonal.application.exception.ValidationException;
import br.com.fullcycle.hexagonal.application.usecase.CreatePartnerUseCase;
import br.com.fullcycle.hexagonal.application.usecase.GetPartnerByIdUseCase;
import br.com.fullcycle.hexagonal.infrastructure.dtos.NewPartnerDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Objects;

@RestController
@RequestMapping(value = "partners")
public class PartnerController {

    private final CreatePartnerUseCase createPartnerUseCase;
    private final GetPartnerByIdUseCase getPartnerByIdUseCase;

    public PartnerController(
            final CreatePartnerUseCase createPartnerUseCase,
            final GetPartnerByIdUseCase getPartnerByIdUseCase
    ) {
        this.createPartnerUseCase = Objects.requireNonNull(createPartnerUseCase);
        this.getPartnerByIdUseCase = Objects.requireNonNull(getPartnerByIdUseCase);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody NewPartnerDTO dto) {
        try {
            final var input = new CreatePartnerUseCase.Input(dto.getCnpj(), dto.getEmail(), dto.getName());
            final var output = createPartnerUseCase.execute(input);
            return ResponseEntity.created(URI.create("/partners/" + output.id())).body(output);
        } catch (ValidationException ex) {
            return ResponseEntity.unprocessableEntity().body(ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        final var input = new GetPartnerByIdUseCase.Input(id);
        return getPartnerByIdUseCase.execute(input)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

}
