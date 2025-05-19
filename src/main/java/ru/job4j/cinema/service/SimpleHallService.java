package ru.job4j.cinema.service;

import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.repository.HallRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SimpleHallService implements HallService {

    private final HallRepository hallRepository;

    public SimpleHallService(HallRepository sql2oHallRepository) {
        this.hallRepository = sql2oHallRepository;
    }

    @Override
    public Optional<Hall> findById(int id) {
        return hallRepository.findById(id);
    }

    @Override
    public List<Hall> findAll() {
        return hallRepository.findAll();
    }
}
