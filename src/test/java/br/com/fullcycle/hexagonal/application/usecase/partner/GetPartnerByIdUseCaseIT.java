package br.com.fullcycle.hexagonal.application.usecase.partner;

import br.com.fullcycle.hexagonal.IntegrationTest;
import br.com.fullcycle.hexagonal.infrastructure.jpa.entities.PartnerEntity;
import br.com.fullcycle.hexagonal.infrastructure.jpa.repositories.PartnerJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

class GetPartnerByIdUseCaseIT extends IntegrationTest {

    @Autowired
    private GetPartnerByIdUseCase useCase;

    @Autowired
    private PartnerJpaRepository partnerRepository;

    @AfterEach
    void tearDown() {
        partnerRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve obter um parceiro por id")
    void testGetById() {
        // given
        final var expectedCNPJ = "12345678";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";
        final var partner = createPartner(expectedCNPJ, expectedEmail, expectedName);
        final var expectedID = partner.getId();

        final var input = new GetPartnerByIdUseCase.Input(expectedID.toString());

        // when
        final var output = useCase.execute(input).get();

        // then
        Assertions.assertEquals(expectedID, output.id());
        Assertions.assertEquals(expectedCNPJ, output.cnpj());
        Assertions.assertEquals(expectedEmail, output.email());
        Assertions.assertEquals(expectedName, output.name());
    }

    @Test
    @DisplayName("Deve obter vazio ao tentar recuperar um parceiro não existente por id")
    void testGetByIdWithInvalidId() {
        // given
        final var expectedID = UUID.randomUUID().toString();

        final var input = new GetPartnerByIdUseCase.Input(expectedID);

        // when
        final var output = useCase.execute(input);

        // then
        Assertions.assertTrue(output.isEmpty());
    }

    private PartnerEntity createPartner(final String cnpj, final String email, final String name) {
        final var aPartner = new PartnerEntity();
        aPartner.setCnpj(cnpj);
        aPartner.setEmail(email);
        aPartner.setName(name);

        return partnerRepository.save(aPartner);
    }
}