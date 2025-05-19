package ru.job4j.cinema.controller;

import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.FilmSessionService;
import ru.job4j.cinema.service.TicketService;
import jakarta.servlet.http.HttpSession;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tickets")
@ThreadSafe
public class TicketController {

    private final TicketService ticketService;
    private final FilmSessionService filmSessionService;

    public TicketController(TicketService ticketService, FilmSessionService filmSessionService) {
        this.ticketService = ticketService;
        this.filmSessionService = filmSessionService;
    }

    @PostMapping("/buy")
    public String buyTicket(@ModelAttribute Ticket ticket, Model model, HttpSession session) {
        var user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setFullName("Гость");
            model.addAttribute("error", "Для покупки билета необходимо войти на сайт.");
            return "users/login";
        }
        model.addAttribute("user", user);
        var ticketOptional = ticketService.findTicketByRowAndPlace(ticket.getSessionId(), ticket.getRowNumber(),
                ticket.getPlaceNumber());
        if (!ticketOptional.isEmpty()) {
            model.addAttribute("message", "Не удалось приобрести билет на выбранное место. "
                    + "Вероятно оно уже занято. Перейдите на страницу бронирования билетов и попробуйте снова.");
            return "tickets/fail";
        }

        var filmSession = filmSessionService.findById(ticket.getSessionId());
        ticketService.save(ticket);
        model.addAttribute("message", user.getFullName() + ", Вы успешно приобрели билет на фильм " + filmSession.get().getFilmName()
                 + " Ваш зал: " + filmSession.get().getHallName()
                 + " Ряд: " + ticket.getRowNumber()
                 + " Место: " + ticket.getPlaceNumber());
        return "tickets/luck";
    }
}