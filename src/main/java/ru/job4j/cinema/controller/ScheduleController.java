package ru.job4j.cinema.controller;

import ru.job4j.cinema.service.FilmSessionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.IntStream;

@Controller
public class ScheduleController {

    private final FilmSessionService filmSessionService;

    public ScheduleController(FilmSessionService filmSessionService) {
        this.filmSessionService = filmSessionService;
    }

    @GetMapping("/schedule")
    public String getSchedule(Model model) {
        model.addAttribute("filmsessionsdto", filmSessionService.findAll());
        return "film/schedule";
    }

    @GetMapping("/one/{filmsessionId}")
    public String getById(Model model, @PathVariable int filmsessionId) {
        var filmSessionOptional = filmSessionService.findById(filmsessionId);
        if (filmSessionOptional.isEmpty()) {
            model.addAttribute("message", "Киносеанс с указанным идентификатором не найден.");
            return "errors/409";
        }
        List<Integer> places = IntStream.rangeClosed(1, filmSessionOptional.get().getPlaceCount()).boxed().toList();
        List<Integer> rows = IntStream.rangeClosed(1, filmSessionOptional.get().getRowCount()).boxed().toList();
        model.addAttribute("filmsessiondto", filmSessionOptional.get());
        model.addAttribute("rows", rows);
        model.addAttribute("places", places);
        return "tickets/one";
    }
}