package ru.job4j.cinema.service;

import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class SimpleTicketService implements TicketService {

    private final TicketRepository ticketRepository;

    public SimpleTicketService(TicketRepository sql2oTicketRepository) {
        this.ticketRepository = sql2oTicketRepository;
    }

    @Override
    public Optional<Ticket> save(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public void deleteById(int id) {
        ticketRepository.deleteById(id);
    }

    @Override
    public Optional<Ticket> findById(int id) {
        return ticketRepository.findById(id);
    }

    @Override
    public Collection<Ticket> findAll() {
        return ticketRepository.findAll();
    }

    @Override
    public Optional<Ticket> findTicketByRowAndPlace(int sessionId, int rowNumber, int placeNumber) {
        return ticketRepository.findTicketByRowAndPlace(sessionId, rowNumber, placeNumber);
    }
}
