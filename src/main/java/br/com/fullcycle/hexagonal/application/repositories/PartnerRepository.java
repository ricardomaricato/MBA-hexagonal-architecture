package br.com.fullcycle.hexagonal.application.repositories;

import br.com.fullcycle.hexagonal.infrastructure.entities.Partner;
import br.com.fullcycle.hexagonal.infrastructure.entities.PartnerId;

import java.util.Optional;

public interface PartnerRepository {

    Optional<Partner> partnerOfId(PartnerId anId);

    Optional<Partner> partnerOfCNPJ(String cnpj);

    Optional<Partner> partnerOfEmail(String email);

    Partner create(Partner partner);

    Partner update(Partner partner);
}
