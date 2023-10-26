package br.com.fullcycle.hexagonal.infrastructure.entities;

import br.com.fullcycle.hexagonal.application.exception.ValidationException;

public class Customer {

    private CustomerId customerId;
    private String name;
    private String cpf;
    private String email;

    public Customer(final CustomerId customerId, final String name, final String cpf, final String email) {
        if (customerId == null) {
            throw new ValidationException("Invalid customerId for Customer");
        }

        if (name == null) {
            throw new ValidationException("Invalid name for Customer");
        }

        if (cpf == null) {
            throw new ValidationException("Invalid cpf for Customer");
        }

        if (email == null) {
            throw new ValidationException("Invalid email for Customer");
        }

        this.customerId = customerId;
        this.name = name;
        this.cpf = cpf;
        this.email = email;
    }

    public static Customer newCustomer(String name, String cpf, String email) {
        return new Customer(CustomerId.unique(), name, cpf, email);
    }

    public CustomerId customerId() {
        return customerId;
    }

    public String name() {
        return name;
    }

    public String cpf() {
        return cpf;
    }

    public String email() {
        return email;
    }
}
