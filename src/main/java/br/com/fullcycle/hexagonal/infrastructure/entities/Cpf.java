package br.com.fullcycle.hexagonal.infrastructure.entities;

import br.com.fullcycle.hexagonal.application.exception.ValidationException;

public record Cpf(String value) {

    public Cpf {
        if (value == null || !value.matches("^\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}$")) {
            throw new ValidationException("Invalid value for Cpf");
        }
    }
}
