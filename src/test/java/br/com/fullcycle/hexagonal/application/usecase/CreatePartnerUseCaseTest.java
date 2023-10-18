package br.com.fullcycle.hexagonal.application.usecase;

import br.com.fullcycle.hexagonal.application.exception.ValidationException;
import br.com.fullcycle.hexagonal.models.Partner;
import br.com.fullcycle.hexagonal.services.PartnerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CreatePartnerUseCaseTest {

    @Test
    @DisplayName("Deve criar um Parceiro")
    void testCreatePartner() {
        // given
        final var expectedCnpj = "12345678";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";

        final var input = new CreatePartnerUseCase.Input(expectedCnpj, expectedEmail, expectedName);

        // when
        final var partnerService = Mockito.mock(PartnerService.class);
        when(partnerService.findByCnpj(expectedCnpj)).thenReturn(Optional.empty());
        when(partnerService.findByEmail(expectedEmail)).thenReturn(Optional.empty());
        when(partnerService.save(any())).thenAnswer(a -> {
            var customer = a.getArgument(0, Partner.class);
            customer.setId(UUID.randomUUID().getMostSignificantBits());
            return customer;
        });

        final var useCase = new CreatePartnerUseCase(partnerService);
        final var output = useCase.execute(input);

        // then
        Assertions.assertNotNull(output.id());
        Assertions.assertEquals(expectedCnpj, output.cnpj());
        Assertions.assertEquals(expectedEmail, output.email());
        Assertions.assertEquals(expectedName, output.name());
    }

    @Test
    @DisplayName("Não deve cadastrar um parceiro com CNPJ duplicado")
    void testCreateWithDuplicatedCNPJShouldFail() {
        // given
        final var expectedCnpj = "12345678";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";
        final var expectedError = "Partner already exists";

        final var input = new CreatePartnerUseCase.Input(expectedCnpj, expectedEmail, expectedName);

        final var aPartner = new Partner();
        aPartner.setId(UUID.randomUUID().getMostSignificantBits());
        aPartner.setCnpj(expectedCnpj);
        aPartner.setEmail(expectedEmail);
        aPartner.setName(expectedName);

        // when
        final var partnerService = Mockito.mock(PartnerService.class);
        when(partnerService.findByCnpj(expectedCnpj)).thenReturn(Optional.of(aPartner));

        final var useCase = new CreatePartnerUseCase(partnerService);
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(input));

        // then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve cadastrar um parceiro com e-mail duplicado")
    void testCreateWithDuplicatedEmailShouldFail() {
        // given
        final var expectedCnpj = "12345678";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";
        final var expectedError = "Partner already exists";

        final var input = new CreatePartnerUseCase.Input(expectedCnpj, expectedEmail, expectedName);

        final var aPartner = new Partner();
        aPartner.setId(UUID.randomUUID().getMostSignificantBits());
        aPartner.setCnpj(expectedCnpj);
        aPartner.setEmail(expectedEmail);
        aPartner.setName(expectedName);

        // when
        final var partnerService = Mockito.mock(PartnerService.class);
        when(partnerService.findByEmail(expectedEmail)).thenReturn(Optional.of(aPartner));

        final var useCase = new CreatePartnerUseCase(partnerService);
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(input));

        // then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }
}