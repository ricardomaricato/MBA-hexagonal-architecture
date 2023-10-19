package br.com.fullcycle.hexagonal.infrastructure.graphql;

import br.com.fullcycle.hexagonal.application.usecase.CreateEventUseCase;
import br.com.fullcycle.hexagonal.application.usecase.SubscriberCustomerToEventUseCase;
import br.com.fullcycle.hexagonal.infrastructure.dtos.NewEventDTO;
import br.com.fullcycle.hexagonal.infrastructure.dtos.SubscribeDTO;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Controller
public class EventResolver {

    private final CreateEventUseCase createEventUseCase;
    private final SubscriberCustomerToEventUseCase subscriberCustomerToEventUseCase;

    public EventResolver(
            final CreateEventUseCase createEventUseCase,
            final SubscriberCustomerToEventUseCase subscriberCustomerToEventUseCase
    ) {
        this.createEventUseCase = Objects.requireNonNull(createEventUseCase);
        this.subscriberCustomerToEventUseCase = Objects.requireNonNull(subscriberCustomerToEventUseCase);
    }

    @MutationMapping
    public CreateEventUseCase.Output createEvent(@Argument NewEventDTO dto) {
            final var input = new CreateEventUseCase.Input(dto.date(), dto.date(), dto.partnerId(), dto.totalSpots());
            return createEventUseCase.execute(input);
    }

    @Transactional
    @MutationMapping
    public SubscriberCustomerToEventUseCase.Output subscribeCustomerToEvent(@Argument SubscribeDTO dto) {
        final var input = new SubscriberCustomerToEventUseCase.Input(dto.customerId(), dto.eventId());
        return subscriberCustomerToEventUseCase.execute(input);
    }
}
