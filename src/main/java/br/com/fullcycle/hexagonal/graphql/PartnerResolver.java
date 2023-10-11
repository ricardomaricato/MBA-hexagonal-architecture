package br.com.fullcycle.hexagonal.graphql;

import br.com.fullcycle.hexagonal.application.usecase.CreatePartnerUseCase;
import br.com.fullcycle.hexagonal.application.usecase.GetPartnerByIdUseCase;
import br.com.fullcycle.hexagonal.dtos.PartnerDTO;
import br.com.fullcycle.hexagonal.services.PartnerService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;

public class PartnerResolver {

    private final PartnerService partnerService;

    public PartnerResolver(PartnerService partnerService) {
        this.partnerService = partnerService;
    }

    @MutationMapping
    public CreatePartnerUseCase.Output createPartner(@Argument PartnerDTO dto) {
        final var useCase = new CreatePartnerUseCase(partnerService);
        final var input = new CreatePartnerUseCase.Input(dto.getCnpj(), dto.getEmail(), dto.getName());
        return useCase.execute(input);
    }

    @QueryMapping
    public GetPartnerByIdUseCase.Output partnerOfId(@Argument Long id) {
        final var useCase = new GetPartnerByIdUseCase(partnerService);
        final var input = new GetPartnerByIdUseCase.Input(id);
        return useCase.execute(input).orElse(null);
    }
}
