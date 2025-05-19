package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.Hall;
import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;

import java.util.List;
import java.util.Optional;

@Repository
public class Sql2oHallRepository implements HallRepository {

    private final Sql2o sql2o;

    public Sql2oHallRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<Hall> findById(int id) {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("""
                    SELECT id, name, row_count AS rowCount, place_count AS placeCount, description 
                    FROM halls WHERE id = :id 
                    """);

            var hall = query.addParameter("id", id).executeAndFetchFirst(Hall.class);
            return Optional.ofNullable(hall);
        }
    }

    @Override
    public List<Hall> findAll() {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("SELECT id, name, row_count AS rowCount, place_count AS placeCount, description FROM halls");
            return query.executeAndFetch(Hall.class);
        }
    }
}
