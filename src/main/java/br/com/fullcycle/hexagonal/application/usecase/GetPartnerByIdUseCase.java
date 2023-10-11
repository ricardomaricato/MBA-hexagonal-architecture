package br.com.fullcycle.hexagonal.application.usecase;

import br.com.fullcycle.hexagonal.application.UseCase;
import br.com.fullcycle.hexagonal.services.PartnerService;

import java.util.Optional;

public class GetPartnerByIdUseCase
        extends UseCase<GetPartnerByIdUseCase.Input, Optional<GetPartnerByIdUseCase.Output>> {

    private final PartnerService partnerService;

    public GetPartnerByIdUseCase(PartnerService partnerService) {
        this.partnerService = partnerService;
    }

    @Override
    public Optional<Output> execute(final Input input) {
        return partnerService.findById(input.id)
                .map(p -> new Output(p.getId(), p.getCnpj(), p.getEmail(), p.getName()));
    }

    public record Input(Long id) {}

    public record Output(Long id, String cnpj, String email, String name) {}
}
