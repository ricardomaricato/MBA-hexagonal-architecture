package br.com.fullcycle.hexagonal.infrastructure.rest;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.usecases.customer.CreateCustomerUseCase;
import br.com.fullcycle.hexagonal.application.usecases.customer.GetCustomerByIdUseCase;
import br.com.fullcycle.hexagonal.infrastructure.dtos.NewCustomerDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Objects;

@RestController
@RequestMapping(value = "customers")
public class CustomerController {

    private final CreateCustomerUseCase createCustomerUseCase;
    private final GetCustomerByIdUseCase getCustomerByIdUseCase;

    public CustomerController(
            final CreateCustomerUseCase createCustomerUseCase,
            final GetCustomerByIdUseCase getCustomerByIdUseCase
    ) {
        this.createCustomerUseCase = Objects.requireNonNull(createCustomerUseCase);
        this.getCustomerByIdUseCase = Objects.requireNonNull(getCustomerByIdUseCase);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody NewCustomerDTO dto) {
       try {
           final var input = new CreateCustomerUseCase.Input(dto.cpf(), dto.email(), dto.name());
           final var output = createCustomerUseCase.execute(input);
           return ResponseEntity.created(URI.create("/customers/" + output.id())).body(output);
       } catch (ValidationException ex) {
            return ResponseEntity.unprocessableEntity().body(ex.getMessage());
       }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable String id) {
        final var input = new GetCustomerByIdUseCase.Input(id);
        return getCustomerByIdUseCase.execute(input)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }
}