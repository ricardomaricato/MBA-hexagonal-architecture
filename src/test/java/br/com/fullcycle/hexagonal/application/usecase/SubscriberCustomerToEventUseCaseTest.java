package br.com.fullcycle.hexagonal.application.usecase;

import br.com.fullcycle.hexagonal.application.exception.ValidationException;
import br.com.fullcycle.hexagonal.infrastructure.models.Customer;
import br.com.fullcycle.hexagonal.infrastructure.models.Event;
import br.com.fullcycle.hexagonal.infrastructure.models.Ticket;
import br.com.fullcycle.hexagonal.infrastructure.models.TicketStatus;
import br.com.fullcycle.hexagonal.infrastructure.services.CustomerService;
import br.com.fullcycle.hexagonal.infrastructure.services.EventService;
import io.hypersistence.tsid.TSID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class SubscriberCustomerToEventUseCaseTest {

    @Test
    @DisplayName("Deve comprar um ticket de um evento")
    void testReserveTicket() {
        // given
        final var expectedTicketSize = 1;

        final var customerID = TSID.fast().toLong();
        final var eventID = TSID.fast().toLong();

        final var aEvent = new Event();
        aEvent.setId(eventID);
        aEvent.setName("Disney");
        aEvent.setTotalSpots(10);

        final var subscribeInput =
                new SubscriberCustomerToEventUseCase.Input(customerID, eventID);

        // when
        final var customerService = Mockito.mock(CustomerService.class);
        final var eventService = Mockito.mock(EventService.class);

        when(customerService.findById(customerID)).thenReturn(Optional.empty());
        when(eventService.findById(eventID)).thenReturn(Optional.of(aEvent));
        when(eventService.findTicketByEventIdAndCustomerId(eventID, customerID)).thenReturn(Optional.empty());
        when(eventService.save(any())).thenAnswer(a -> {
            final var e = a.getArgument(0, Event.class);
            Assertions.assertEquals(expectedTicketSize, e.getTickets().size());
            return e;
        });

        final var useCase = new SubscriberCustomerToEventUseCase(customerService, eventService);
        final var output = useCase.execute(subscribeInput);

        // then
        Assertions.assertEquals(eventID, output.eventId());
        Assertions.assertNotNull(output.reservetionDate());
        Assertions.assertEquals(TicketStatus.PENDING.name(), output.ticketStatus());
    }

    @Test
    @DisplayName("Não deve comprar um ticket de um cliente não existente")
    void testReserveTicketWithoutCustomer() {
        // given
        final var expectedError = "Customer not found";

        final var customerID = TSID.fast().toLong();
        final var eventID = TSID.fast().toLong();

        final var subscribeInput =
                new SubscriberCustomerToEventUseCase.Input(customerID, eventID);

        // when
        final var customerService = Mockito.mock(CustomerService.class);
        final var eventService = Mockito.mock(EventService.class);

        when(customerService.findById(customerID)).thenReturn(Optional.empty());

        final var useCase = new SubscriberCustomerToEventUseCase(customerService, eventService);
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(subscribeInput));

        // then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve comprar um ticket de um evento que não existe")
    void testReserveTicketWithoutEvent() {
        // given
        final var expectedError = "Event not found";

        final var customerID = TSID.fast().toLong();
        final var eventID = TSID.fast().toLong();

        final var subscribeInput =
                new SubscriberCustomerToEventUseCase.Input(customerID, eventID);

        // when
        final var customerService = Mockito.mock(CustomerService.class);
        final var eventService = Mockito.mock(EventService.class);

        when(customerService.findById(customerID)).thenReturn(Optional.of(new Customer()));
        when(eventService.findById(eventID)).thenReturn(Optional.empty());

        final var useCase = new SubscriberCustomerToEventUseCase(customerService, eventService);
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(subscribeInput));

        // then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }

    @Test
    @DisplayName("Um mesmo cliente não deve comprar mais de um ticket por evento")
    void testReserveTicketMoreThanOnce() {
        // given
        final var expectedError = "Email already registered";

        final var customerID = TSID.fast().toLong();
        final var eventID = TSID.fast().toLong();

        final var aEvent = new Event();
        aEvent.setId(eventID);
        aEvent.setName("Disney");
        aEvent.setTotalSpots(10);

        final var subscribeInput =
                new SubscriberCustomerToEventUseCase.Input(customerID, eventID);

        // when
        final var customerService = Mockito.mock(CustomerService.class);
        final var eventService = Mockito.mock(EventService.class);

        when(customerService.findById(customerID)).thenReturn(Optional.of(new Customer()));
        when(eventService.findById(eventID)).thenReturn(Optional.of(aEvent));
        when(eventService.findTicketByEventIdAndCustomerId(eventID, customerID)).thenReturn(Optional.of(new Ticket()));

        final var useCase = new SubscriberCustomerToEventUseCase(customerService, eventService);
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(subscribeInput));

        // then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }

    @Test
    @DisplayName("Um mesmo cliente não deve comprar de um evento que não há mais cadeiras")
    void testReserveTicketWithoutSlots() {
        // given
        final var expectedError = "Event sold out";

        final var customerID = TSID.fast().toLong();
        final var eventID = TSID.fast().toLong();

        final var aEvent = new Event();
        aEvent.setId(eventID);
        aEvent.setName("Disney");
        aEvent.setTotalSpots(0);

        final var subscribeInput =
                new SubscriberCustomerToEventUseCase.Input(customerID, eventID);

        // when
        final var customerService = Mockito.mock(CustomerService.class);
        final var eventService = Mockito.mock(EventService.class);

        when(customerService.findById(customerID)).thenReturn(Optional.of(new Customer()));
        when(eventService.findById(eventID)).thenReturn(Optional.of(aEvent));
        when(eventService.findTicketByEventIdAndCustomerId(eventID, customerID)).thenReturn(Optional.empty());

        final var useCase = new SubscriberCustomerToEventUseCase(customerService, eventService);
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(subscribeInput));

        // then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }
}