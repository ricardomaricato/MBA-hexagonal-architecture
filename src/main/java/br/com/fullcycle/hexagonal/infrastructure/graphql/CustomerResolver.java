package br.com.fullcycle.hexagonal.infrastructure.graphql;

import br.com.fullcycle.hexagonal.application.usecases.customer.CreateCustomerUseCase;
import br.com.fullcycle.hexagonal.application.usecases.customer.GetCustomerByIdUseCase;
import br.com.fullcycle.hexagonal.infrastructure.dtos.NewCustomerDTO;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.Objects;

@Controller
public class CustomerResolver {

    private final CreateCustomerUseCase createCustomerUseCase;
    private final GetCustomerByIdUseCase getCustomerByIdUseCase;

    public CustomerResolver(
            final CreateCustomerUseCase createCustomerUseCase,
            final GetCustomerByIdUseCase getCustomerByIdUseCase
    ) {
        this.createCustomerUseCase = Objects.requireNonNull(createCustomerUseCase);
        this.getCustomerByIdUseCase = Objects.requireNonNull(getCustomerByIdUseCase);
    }

    @MutationMapping
    public CreateCustomerUseCase.Output createCustomer(@Argument NewCustomerDTO dto) {
        final var input = new CreateCustomerUseCase.Input(dto.cpf(), dto.email(), dto.name());
        return createCustomerUseCase.execute(input);
    }

    @QueryMapping
    public GetCustomerByIdUseCase.Output customerOfId(@Argument String id) {
        final var input = new GetCustomerByIdUseCase.Input(id);
        return getCustomerByIdUseCase.execute(input).orElse(null);
    }
}
