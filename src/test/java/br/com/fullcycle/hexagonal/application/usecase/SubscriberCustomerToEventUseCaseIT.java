package br.com.fullcycle.hexagonal.application.usecase;

import br.com.fullcycle.hexagonal.IntegrationTest;
import br.com.fullcycle.hexagonal.application.exception.ValidationException;
import br.com.fullcycle.hexagonal.infrastructure.models.Customer;
import br.com.fullcycle.hexagonal.infrastructure.models.Event;
import br.com.fullcycle.hexagonal.infrastructure.models.Ticket;
import br.com.fullcycle.hexagonal.infrastructure.models.TicketStatus;
import br.com.fullcycle.hexagonal.infrastructure.repositories.CustomerRepository;
import br.com.fullcycle.hexagonal.infrastructure.repositories.EventRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

class SubscriberCustomerToEventUseCaseIT extends IntegrationTest {

    @Autowired
    private SubscriberCustomerToEventUseCase useCase;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EventRepository eventRepository;

    @AfterEach
    void tearDown() {
        customerRepository.deleteAll();
        eventRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve comprar um ticket de um evento")
    void testReserveTicket() {
        // given
        final var customer = createCustomer("4159873000100", "john.coe@gmail.com", "John Coe");
        final var event = createEvent("Disney", 10);

        final var customerID = customer.getId();
        final var eventID = event.getId();

        final var subscribeInput =
                new SubscriberCustomerToEventUseCase.Input(customerID, eventID);

        // when
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

        final var subscribeInput =
                new SubscriberCustomerToEventUseCase.Input(Long.MIN_VALUE, Long.MAX_VALUE);

        // when
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(subscribeInput));

        // then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve comprar um ticket de um evento que não existe")
    void testReserveTicketWithoutEvent() {
        // given
        final var customer = createCustomer("4159873000100", "john.coe@gmail.com", "John Coe");
        final var expectedError = "Event not found";

        final var customerID = customer.getId();

        final var subscribeInput =
                new SubscriberCustomerToEventUseCase.Input(customerID, Long.MIN_VALUE);

        // when
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(subscribeInput));

        // then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }

    @Test
    @DisplayName("Um mesmo cliente não deve comprar mais de um ticket por evento")
    void testReserveTicketMoreThanOnce() {
        // given
        final var customer = createCustomer("4159873000100", "john.coe@gmail.com", "John Coe");
        final var event = createEvent("Disney", 10);
        final var expectedError = "Email already registered";

        final var customerID = customer.getId();
        final var eventID = event.getId();

        createTicket(event, customer);

        final var subscribeInput =
                new SubscriberCustomerToEventUseCase.Input(customerID, eventID);

        // when
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(subscribeInput));

        // then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }

    @Test
    @DisplayName("Um mesmo cliente não deve comprar um evento que não há mais cadeiras")
    void testReserveTicketWithoutSlots() {
        // given
        final var customer = createCustomer("4159873000100", "john.coe@gmail.com", "John Coe");
        final var event = createEvent("Disney", 0);
        final var expectedError = "Event sold out";

        final var customerID = customer.getId();
        final var eventID = event.getId();

        final var subscribeInput =
                new SubscriberCustomerToEventUseCase.Input(customerID, eventID);

        // when
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(subscribeInput));

        // then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }

    private Customer createCustomer(final String cpf, final String email, final String name) {
        final var aCustomer = new Customer();
        aCustomer.setId(UUID.randomUUID().getMostSignificantBits());
        aCustomer.setCpf(cpf);
        aCustomer.setEmail(email);
        aCustomer.setName(name);

        return customerRepository.save(aCustomer);
    }

    private Event createEvent(final String name, final int totalSpots) {
        final var aEvent = new Event();
        aEvent.setId(UUID.randomUUID().getMostSignificantBits());
        aEvent.setName(name);
        aEvent.setTotalSpots(totalSpots);

        return eventRepository.save(aEvent);
    }

    private void createTicket(final Event event, final Customer customer) {
        final var aTicket = new Ticket();
        aTicket.setEvent(event);
        aTicket.setCustomer(customer);
        event.getTickets().add(aTicket);

        eventRepository.save(event);
    }
}