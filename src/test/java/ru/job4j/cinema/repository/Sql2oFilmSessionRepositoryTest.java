package ru.job4j.cinema.repository;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Query;

import static org.mockito.Mockito.*;
import ru.job4j.cinema.model.FilmSession;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class Sql2oFilmSessionRepositoryTest {

    @Test
    public void whenFindAllSessions() {
        Sql2o sql2oMock = mock(Sql2o.class);
        Connection connectionMock = mock(Connection.class);
        Query queryMock = mock(Query.class);

        when(sql2oMock.open()).thenReturn(connectionMock);
        when(connectionMock.createQuery("SELECT * FROM film_sessions")).thenReturn(queryMock);

        when(queryMock.setColumnMappings(FilmSession.COLUMN_MAPPING)).thenReturn(queryMock);

        FilmSession session1 = new FilmSession(1, 1, 1, LocalDateTime.now().plusHours(4), LocalDateTime.now().plusHours(6), 300);
        FilmSession session2 = new FilmSession(2, 2, 2, LocalDateTime.now().plusHours(7), LocalDateTime.now().plusHours(9), 350);
        List<FilmSession> expectedSessions = Arrays.asList(session1, session2);

        when(queryMock.executeAndFetch(FilmSession.class)).thenReturn(expectedSessions);

        Sql2oFilmSessionRepository repo = new Sql2oFilmSessionRepository(sql2oMock);

        List<FilmSession> result = (List<FilmSession>) repo.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
    }
}
