package br.com.fullcycle.hexagonal.application.entities;

import br.com.fullcycle.hexagonal.application.exception.ValidationException;

import java.util.UUID;

public record EventId(String value) {

    public static EventId unique() {
        return new EventId(UUID.randomUUID().toString());
    }

    public static EventId with(final String value) {
        try {
            return new EventId(UUID.fromString(value).toString());
        } catch (IllegalArgumentException ex) {
            throw new ValidationException("Invalid value for EventId");
        }
    }
}
