package br.com.fullcycle.hexagonal.infrastructure.entities;

import br.com.fullcycle.hexagonal.application.exception.ValidationException;

public class Partner {

    private CustomerId partnerId;
    private Name name;
    private Cnpj cnpj;
    private Email email;

    public Partner(final CustomerId partnerId, final String name, final String cnpj, final String email) {
        if (partnerId == null) {
            throw new ValidationException("Invalid partnerId for Partner");
        }

        this.partnerId = partnerId;
        this.name = new Name(name);
        this.cnpj = new Cnpj(cnpj);
        this.email = new Email(email);
    }

    public static Partner newPartner(String name, String cnpj, String email) {
        return new Partner(CustomerId.unique(), name, cnpj, email);
    }

    public CustomerId partnerId() {
        return partnerId;
    }

    public Name name() {
        return name;
    }

    public Cnpj cnpj() {
        return cnpj;
    }

    public Email email() {
        return email;
    }
}
