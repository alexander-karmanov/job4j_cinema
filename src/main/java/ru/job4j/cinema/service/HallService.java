package ru.job4j.cinema.service;

import ru.job4j.cinema.model.Hall;

import java.util.List;
import java.util.Optional;

public interface HallService {
    Optional<Hall> findById(int id);

    List<Hall> findAll();
}
