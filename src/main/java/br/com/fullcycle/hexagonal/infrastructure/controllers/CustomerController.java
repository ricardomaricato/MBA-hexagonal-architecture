package br.com.fullcycle.hexagonal.infrastructure.controllers;

import br.com.fullcycle.hexagonal.application.exception.ValidationException;
import br.com.fullcycle.hexagonal.application.usecase.CreateCustomerUseCase;
import br.com.fullcycle.hexagonal.application.usecase.GetCustomerByIdUseCase;
import br.com.fullcycle.hexagonal.infrastructure.dtos.CustomerDTO;
import br.com.fullcycle.hexagonal.infrastructure.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(value = "customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CustomerDTO dto) {
       try {
           final var useCase = new CreateCustomerUseCase(customerService);
           final var input = new CreateCustomerUseCase.Input(dto.getCpf(), dto.getEmail(), dto.getName());
           final var output = useCase.execute(input);
           return ResponseEntity.created(URI.create("/customers/" + output.id())).body(output);
       } catch (ValidationException ex) {
            return ResponseEntity.unprocessableEntity().body(ex.getMessage());
       }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        final var useCase = new GetCustomerByIdUseCase(customerService);
        final var input = new GetCustomerByIdUseCase.Input(id);
        return useCase.execute(input)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }
}