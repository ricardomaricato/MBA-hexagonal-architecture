package br.com.fullcycle.hexagonal.infrastructure.entities;

import br.com.fullcycle.hexagonal.application.exception.ValidationException;

import java.util.UUID;

public record PartnerId(UUID value) {

    public static PartnerId unique() {
        return new PartnerId(UUID.randomUUID());
    }

    public static PartnerId with(final String value) {
        try {
            return new PartnerId(UUID.fromString(value));
        } catch (IllegalArgumentException ex) {
            throw new ValidationException("Invalid value for CustomerId");
        }
    }
}
