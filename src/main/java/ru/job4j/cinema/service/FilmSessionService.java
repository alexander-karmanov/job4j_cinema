package ru.job4j.cinema.service;

import ru.job4j.cinema.dto.FilmSessionDto;

import java.util.List;
import java.util.Optional;

public interface FilmSessionService {
    Optional<FilmSessionDto> findById(int id);

    List<FilmSessionDto> findAll();
}
