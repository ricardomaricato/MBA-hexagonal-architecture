package br.com.fullcycle.hexagonal.infrastructure.graphql;

import br.com.fullcycle.hexagonal.application.usecases.partner.CreatePartnerUseCase;
import br.com.fullcycle.hexagonal.application.usecases.partner.GetPartnerByIdUseCase;
import br.com.fullcycle.hexagonal.infrastructure.dtos.NewPartnerDTO;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;

import java.util.Objects;

public class PartnerResolver {

    private final CreatePartnerUseCase createPartnerUseCase;
    private final GetPartnerByIdUseCase getPartnerByIdUseCase;

    public PartnerResolver(
            final CreatePartnerUseCase createPartnerUseCase,
            final GetPartnerByIdUseCase getPartnerByIdUseCase
    ) {
        this.createPartnerUseCase = Objects.requireNonNull(createPartnerUseCase);
        this.getPartnerByIdUseCase = Objects.requireNonNull(getPartnerByIdUseCase);
    }

    @MutationMapping
    public CreatePartnerUseCase.Output createPartner(@Argument NewPartnerDTO dto) {
        final var input = new CreatePartnerUseCase.Input(dto.cnpj(), dto.email(), dto.name());
        return createPartnerUseCase.execute(input);
    }

    @QueryMapping
    public GetPartnerByIdUseCase.Output partnerOfId(@Argument String id) {
        final var input = new GetPartnerByIdUseCase.Input(id);
        return getPartnerByIdUseCase.execute(input).orElse(null);
    }
}
