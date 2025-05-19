package ru.job4j.cinema.controller;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.model.File;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.model.Genre;
import ru.job4j.cinema.service.FilmService;

import java.util.Arrays;
import java.util.List;

public class FilmLibraryControllerTest {
    private FilmService filmService;

    private FilmLibraryController controller;

    @BeforeEach
    void init() {
        filmService = mock(FilmService.class);
        controller = new FilmLibraryController(filmService);
    }

    @Test
    void whenGetAllFilmsThenReturnAllFilms() {

        Film film1 = new Film("Film1", "description1", 2015, 1, 12, 120, 1);
        Genre genre1 = new Genre("Action", 1);
        File file1 = new File("file1", "file1.jpg");
        Film film2 = new Film("Film2", "description2", 2021, 2, 16, 90, 2);
        Genre genre2 = new Genre("Drama", 2);
        File file2 = new File("file2", "file2.jpg");
        List<FilmDto> filmsDtoList = Arrays.asList(
                new FilmDto(film1, genre1, file1),
                new FilmDto(film2, genre2, file2)
        );

        when(filmService.findAll()).thenReturn(filmsDtoList);
        var model = new ConcurrentModel();
        String view = controller.getAll(model);
        assertThat(view).isEqualTo("film/films");
    }
}
