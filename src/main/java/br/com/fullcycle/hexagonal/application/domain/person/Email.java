package br.com.fullcycle.hexagonal.application.domain.person;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

public record Email(String value) {

    public Email {
        if (value == null || !value.matches("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$")) {
            throw new ValidationException("Invalid value for Email");
        }

    }
}
