package br.com.fullcycle.hexagonal.application.entities;

import br.com.fullcycle.hexagonal.application.exception.ValidationException;

import java.util.UUID;

public record PartnerId(String value) {

    public static PartnerId unique() {
        return new PartnerId(UUID.randomUUID().toString());
    }

    public static PartnerId with(final String value) {
        try {
            return new PartnerId(UUID.fromString(value).toString());
        } catch (IllegalArgumentException ex) {
            throw new ValidationException("Invalid value for CustomerId");
        }
    }
}
