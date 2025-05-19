package ru.job4j.cinema.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;
import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.model.*;
import ru.job4j.cinema.service.FilmSessionService;
import ru.job4j.cinema.service.TicketService;

import java.time.LocalDateTime;
import java.util.Optional;

public class TicketControllerTest {

    private TicketController ticketController;

    private TicketService ticketService;

    private FilmSessionService filmSessionService;

    private HttpSession session;

    private Model model;

    @BeforeEach
    void init() {
        model = mock(Model.class);
        session = mock(HttpSession.class);
        ticketService = mock(TicketService.class);
        filmSessionService = mock(FilmSessionService.class);
        ticketController = new TicketController(ticketService, filmSessionService);
    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void whenBuyTicketSuccessThenLuckPage() {
        User user = new User(1, "Иван Иванов", "ivan@example.ru", "1234567");
        Ticket ticket = new Ticket(0, 1, 2, 3, user.getId());

        when(session.getAttribute("user")).thenReturn(user);

        when(ticketService.findTicketByRowAndPlace(ticket.getSessionId(), ticket.getRowNumber(), ticket.getPlaceNumber()))
                .thenReturn(Optional.empty());

        Film film1 = new Film("Film1", "description1", 2015, 1, 12, 120, 1);
        FilmSession filmSession1 = new FilmSession(1, 1, 1, LocalDateTime.now().plusHours(3),  LocalDateTime.now().plusHours(5), 300);
        Hall hall1 = new Hall(1, "Hall1", 2, 2, "1");
        var filmSessionDto = new FilmSessionDto(film1, filmSession1, hall1);

        when(filmSessionService.findById(ticket.getSessionId()))
                .thenReturn(Optional.of(filmSessionDto));

        String view = ticketController.buyTicket(ticket, model, session);

        assertEquals("tickets/luck", view);
        verify(ticketService).save(any(Ticket.class));
    }

    @Test
    public void whenBuyTicketFailThenFailPage() {
        User user = new User(1, "Анатолий Николаев", "aqnatoliy@example.ru", "7654321");
        Ticket ticket = new Ticket(1, 2, 5, 10, user.getId());
        when(session.getAttribute("user")).thenReturn(user);

        when(ticketService.findTicketByRowAndPlace(anyInt(), anyInt(), anyInt()))
                .thenReturn(Optional.of(new Ticket()));

        String view = ticketController.buyTicket(ticket, model, session);
        assertEquals("tickets/fail", view);
    }
}
