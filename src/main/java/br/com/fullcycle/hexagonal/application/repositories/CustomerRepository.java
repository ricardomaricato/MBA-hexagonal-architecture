package br.com.fullcycle.hexagonal.application.repositories;

import br.com.fullcycle.hexagonal.infrastructure.entities.Customer;
import br.com.fullcycle.hexagonal.infrastructure.entities.CustomerId;

import java.util.Optional;

public interface CustomerRepository {

    Optional<Customer> customerOfId(CustomerId anId);

    Optional<Customer> customerOfCPF(String cpf);

    Optional<Customer> customerOfEmail(String email);

    Customer create(Customer customer);

    Customer update(Customer customer);
}
