package br.com.fullcycle.hexagonal.application.usecase;

import br.com.fullcycle.hexagonal.models.Partner;
import br.com.fullcycle.hexagonal.services.PartnerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;

class GetPartnerByIdUseCaseTest {

    @Test
    @DisplayName("Deve obter um parceiro por id")
    void testGetById() {
        // given
        final var expectedID = UUID.randomUUID().getMostSignificantBits();
        final var expectedCNPJ = "12345678";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";

        final var aPartner = new Partner();
        aPartner.setId(expectedID);
        aPartner.setCnpj(expectedCNPJ);
        aPartner.setEmail(expectedEmail);
        aPartner.setName(expectedName);

        final var input = new GetPartnerByIdUseCase.Input(expectedID);

        // when
        final var partnerService = Mockito.mock(PartnerService.class);
        when(partnerService.findById(expectedID)).thenReturn(Optional.of(aPartner));

        final var useCase = new GetPartnerByIdUseCase(partnerService);
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
        final var expectedID = UUID.randomUUID().getMostSignificantBits();

        final var input = new GetPartnerByIdUseCase.Input(expectedID);

        // when
        final var partnerService = Mockito.mock(PartnerService.class);
        when(partnerService.findById(expectedID)).thenReturn(Optional.empty());

        final var useCase = new GetPartnerByIdUseCase(partnerService);
        final var output = useCase.execute(input);

        // then
        Assertions.assertTrue(output.isEmpty());
    }

}