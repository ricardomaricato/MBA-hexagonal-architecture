package br.com.fullcycle.hexagonal.infrastructure.graphql;

import br.com.fullcycle.hexagonal.application.usecase.CreateCustomerUseCase;
import br.com.fullcycle.hexagonal.application.usecase.GetCustomerByIdUseCase;
import br.com.fullcycle.hexagonal.infrastructure.dtos.CustomerDTO;
import br.com.fullcycle.hexagonal.infrastructure.services.CustomerService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class CustomerResolver {

    private final CustomerService customerService;

    public CustomerResolver(CustomerService customerService) {
        this.customerService = customerService;
    }

    @MutationMapping
    public CreateCustomerUseCase.Output createCustomer(@Argument CustomerDTO dto) {
        final var useCase = new CreateCustomerUseCase(customerService);
        final var input = new CreateCustomerUseCase.Input(dto.getCpf(), dto.getEmail(), dto.getName());
        return useCase.execute(input);
    }

    @QueryMapping
    public GetCustomerByIdUseCase.Output customerOfId(@Argument Long id) {
        final var useCase = new GetCustomerByIdUseCase(customerService);
        final var input = new GetCustomerByIdUseCase.Input(id);
        return useCase.execute(input).orElse(null);
    }
}
