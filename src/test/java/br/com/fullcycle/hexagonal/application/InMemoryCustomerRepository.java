package br.com.fullcycle.hexagonal.application;

import br.com.fullcycle.hexagonal.application.repositories.CustomerRepository;
import br.com.fullcycle.hexagonal.application.entities.Customer;
import br.com.fullcycle.hexagonal.application.entities.CustomerId;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class InMemoryCustomerRepository implements CustomerRepository {

    private final Map<String, Customer> customers;
    private final Map<String, Customer> customersByCPF;
    private final Map<String, Customer> customersByEmail;

    public InMemoryCustomerRepository() {
        this.customers = new HashMap<>();
        this.customersByCPF = new HashMap<>();
        this.customersByEmail = new HashMap<>();
    }

    @Override
    public Optional<Customer> customerOfId(CustomerId anId) {
        return Optional.ofNullable(this.customers.get(Objects.requireNonNull(anId).value().toString()));
    }

    @Override
    public Optional<Customer> customerOfCPF(String cpf) {
        return Optional.ofNullable(this.customers.get(Objects.requireNonNull(cpf)));
    }

    @Override
    public Optional<Customer> customerOfEmail(String email) {
        return Optional.ofNullable(this.customers.get(Objects.requireNonNull(email)));
    }

    @Override
    public Customer create(Customer customer) {
        this.customers.put(customer.customerId().value().toString(), customer);
        this.customers.put(customer.cpf().value(), customer);
        this.customers.put(customer.email().value(), customer);
        return customer;
    }

    @Override
    public Customer update(Customer customer) {
        this.customers.put(customer.customerId().value().toString(), customer);
        this.customers.put(customer.cpf().value(), customer);
        this.customers.put(customer.email().value(), customer);
        return customer;
    }
}
