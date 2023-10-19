package br.com.fullcycle.hexagonal.application.usecase;

import br.com.fullcycle.hexagonal.application.UseCase;
import br.com.fullcycle.hexagonal.application.exception.ValidationException;
import br.com.fullcycle.hexagonal.infrastructure.models.Ticket;
import br.com.fullcycle.hexagonal.infrastructure.models.TicketStatus;
import br.com.fullcycle.hexagonal.infrastructure.services.CustomerService;
import br.com.fullcycle.hexagonal.infrastructure.services.EventService;

import java.time.Instant;
import java.util.Objects;

public class SubscriberCustomerToEventUseCase
        extends UseCase<SubscriberCustomerToEventUseCase.Input, SubscriberCustomerToEventUseCase.Output> {

    private final CustomerService customerService;
    private final EventService eventService;

    public SubscriberCustomerToEventUseCase(final CustomerService customerService, final EventService eventServicel) {
        this.customerService = Objects.requireNonNull(customerService);
        this.eventService = Objects.requireNonNull(eventServicel);
    }

    @Override
    public Output execute(final Input input) {
        var customer = customerService.findById(input.customerId)
                .orElseThrow(() -> new ValidationException("Customer not found"));

        var event = eventService.findById(input.eventId)
                .orElseThrow(() -> new ValidationException("Event not found"));

        eventService.findTicketByEventIdAndCustomerId(input.eventId, input.customerId)
                .ifPresent(t -> {
                    throw new ValidationException("Email already registered");
                });

        if (event.getTotalSpots() < event.getTickets().size() + 1) {
            throw new ValidationException("Event sold out");
        }

        var ticket = new Ticket();
        ticket.setEvent(event);
        ticket.setCustomer(customer);
        ticket.setReservedAt(Instant.now());
        ticket.setStatus(TicketStatus.PENDING);

        event.getTickets().add(ticket);

        eventService.save(event);

        return new Output(ticket.getId(), ticket.getStatus().name(), ticket.getReservedAt());
    }

    public record Input(Long eventId, Long customerId) {}

    public record Output(Long eventId, String ticketStatus, Instant reservetionDate) {}
}
