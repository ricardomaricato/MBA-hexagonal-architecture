package br.com.fullcycle.hexagonal.infrastructure.configuration;

import br.com.fullcycle.hexagonal.application.usecase.*;
import br.com.fullcycle.hexagonal.infrastructure.services.CustomerService;
import br.com.fullcycle.hexagonal.infrastructure.services.EventService;
import br.com.fullcycle.hexagonal.infrastructure.services.PartnerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class UseCaseConfig {

    private final CustomerService customerService;
    private final EventService eventService;
    private final PartnerService partnerService;

    public UseCaseConfig(
            CustomerService customerService,
            EventService eventService,
            PartnerService partnerService
    ) {
        this.customerService = Objects.requireNonNull(customerService);
        this.eventService = Objects.requireNonNull(eventService);
        this.partnerService = Objects.requireNonNull(partnerService);
    }

    @Bean
    public CreateCustomerUseCase createCustomerUseCase() {
        // TODO: fix dependency
        return new CreateCustomerUseCase(null);
    }

    @Bean
    public CreateEventUseCase createEventUseCase() {
        // TODO: fix dependency
        return new CreateEventUseCase(null, null);
    }

    @Bean
    public CreatePartnerUseCase createPartnerUseCase() {
        // TODO: fix dependency
        return new CreatePartnerUseCase(null);
    }

    @Bean
    public GetCustomerByIdUseCase getCustomerByIdUseCase() {
        // TODO: fix dependency
        return new GetCustomerByIdUseCase(null);
    }

    @Bean
    public GetPartnerByIdUseCase getPartnerByIdUseCase() {
        // TODO: fix dependency
        return new GetPartnerByIdUseCase(null);
    }

    @Bean
    public SubscriberCustomerToEventUseCase subscriberCustomerToEventUseCase() {
        return new SubscriberCustomerToEventUseCase(customerService, eventService);
    }
}
