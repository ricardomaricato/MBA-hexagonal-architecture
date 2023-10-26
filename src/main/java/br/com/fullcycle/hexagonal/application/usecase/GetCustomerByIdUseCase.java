package br.com.fullcycle.hexagonal.application.usecase;

import br.com.fullcycle.hexagonal.application.UseCase;
import br.com.fullcycle.hexagonal.application.repositories.CustomerRepository;
import br.com.fullcycle.hexagonal.infrastructure.entities.CustomerId;

import java.util.Objects;
import java.util.Optional;

public class GetCustomerByIdUseCase
        extends UseCase<GetCustomerByIdUseCase.Input, Optional<GetCustomerByIdUseCase.Output>> {

    private final CustomerRepository customerRepository;

    public GetCustomerByIdUseCase(final CustomerRepository customerRepository) {
        this.customerRepository = Objects.requireNonNull(customerRepository);
    }


    @Override
    public Optional<Output> execute(final Input input) {
        return customerRepository.customerOfId(CustomerId.with(input.id))
                .map(c -> new Output(c.customerId().value().toString(), c.cpf(), c.email(), c.name()));
    }

    public record Input(String id) {}

    public record Output(String id, String cpf, String email, String name) {}
}
