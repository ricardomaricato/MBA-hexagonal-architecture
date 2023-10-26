package br.com.fullcycle.hexagonal.application.usecase;

import br.com.fullcycle.hexagonal.application.UseCase;
import br.com.fullcycle.hexagonal.application.exception.ValidationException;
import br.com.fullcycle.hexagonal.application.repositories.CustomerRepository;
import br.com.fullcycle.hexagonal.infrastructure.entities.Customer;

public class CreateCustomerUseCase
        extends UseCase<CreateCustomerUseCase.Input, CreateCustomerUseCase.Output> {

    private final CustomerRepository customerRepository;

    public CreateCustomerUseCase(final CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Output execute(final Input input) {
        if (customerRepository.customerOfCPF(input.cpf).isPresent()) {
            throw new ValidationException("Customer already exists");
        }
        if (customerRepository.customerOfEmail(input.email).isPresent()) {
            throw new ValidationException("Customer already exists");
        }

        var customer = customerRepository.create(Customer.newCustomer(input.name, input.cpf, input.email));

        return new Output(customer.customerId().value().toString(), customer.cpf(), customer.email(), customer.name());
    }

    public record Input(String cpf, String email, String name) {}
    public record Output(String id, String cpf, String email, String name) {}
}
