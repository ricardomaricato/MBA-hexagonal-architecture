package br.com.fullcycle.hexagonal.controllers;

import br.com.fullcycle.hexagonal.application.exception.ValidationException;
import br.com.fullcycle.hexagonal.application.usecase.CreateEventUseCase;
import br.com.fullcycle.hexagonal.application.usecase.SubscriberCustomerToEventUseCase;
import br.com.fullcycle.hexagonal.dtos.EventDTO;
import br.com.fullcycle.hexagonal.dtos.SubscribeDTO;
import br.com.fullcycle.hexagonal.services.CustomerService;
import br.com.fullcycle.hexagonal.services.EventService;
import br.com.fullcycle.hexagonal.services.PartnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Objects;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping(value = "events")
public class EventController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private EventService eventService;

    @Autowired
    private PartnerService partnerService;

    @PostMapping
    @ResponseStatus(CREATED)
    public ResponseEntity<?> create(@RequestBody EventDTO dto) {
        try {
            final var partnerId = Objects.requireNonNull(dto.getPartner(), "Partner is required").getId();
            final var useCase = new CreateEventUseCase(eventService, partnerService);
            final var input = new CreateEventUseCase.Input(dto.getDate(), dto.getName(), partnerId, dto.getTotalSpots());
            final var output = useCase.execute(input);
            return ResponseEntity.created(URI.create("/partners/" + output.id())).body(output);
        } catch (ValidationException ex) {
            return ResponseEntity.unprocessableEntity().body(ex.getMessage());
        }
    }

    @Transactional
    @PostMapping(value = "/{id}/subscribe")
    public ResponseEntity<?> subscribe(@PathVariable Long id, @RequestBody SubscribeDTO dto) {
        try {
            final var useCase = new SubscriberCustomerToEventUseCase(customerService, eventService);
            final var input = new SubscriberCustomerToEventUseCase.Input(id, dto.getCustomerId());
            final var output = useCase.execute(input);
            return ResponseEntity.ok(output);
        } catch (ValidationException ex) {
            return ResponseEntity.unprocessableEntity().body(ex.getMessage());
        }
    }
}
