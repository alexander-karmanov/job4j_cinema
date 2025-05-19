package ru.job4j.cinema.controller;

import ru.job4j.cinema.service.FilmService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FilmLibraryController {

    private final FilmService filmService;

    public FilmLibraryController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public String getAll(Model model) {
        model.addAttribute("filmsdto", filmService.findAll());
        return "film/films";
    }
}

