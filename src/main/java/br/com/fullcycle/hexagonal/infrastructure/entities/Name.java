package br.com.fullcycle.hexagonal.infrastructure.entities;

import br.com.fullcycle.hexagonal.application.exception.ValidationException;

public record Name(String value) {

    public Name {
        if (value == null) {
            throw new ValidationException("Invalid value for name");
        }
    }
}
