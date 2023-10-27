package br.com.fullcycle.hexagonal.infrastructure.entities;

import br.com.fullcycle.hexagonal.application.exception.ValidationException;

public record Cnpj(String value) {

    public Cnpj {
        if (value == null || !value.matches("^\\d{2}\\.\\d{3}\\.\\d{3}\\/\\d{4}\\-\\d{2}$")) {
            throw new ValidationException("Invalid value for Cnpj");
        }
    }
}
