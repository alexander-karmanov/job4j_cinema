package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.Ticket;

import java.util.Optional;
import java.util.Properties;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static java.util.Optional.empty;

public class Sql2oTicketRepositoryTest {

    private static Sql2oTicketRepository sql2oTicketRepository;

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oTicketRepositoryTest.class.getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }

        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        var sql2o = configuration.databaseClient(datasource);

        sql2oTicketRepository = new Sql2oTicketRepository(sql2o);
    }

    @AfterEach
    public void clearTickets() {
        var tickets = sql2oTicketRepository.findAll();
        for (var ticket : tickets) {
            sql2oTicketRepository.deleteById(ticket.getId());
        }
    }

    @Test
    public void whenSaveTicketThenGetSame() {
        var ticket = sql2oTicketRepository.save(new Ticket(1, 1, 2, 3, 1));
        var savedTicket = sql2oTicketRepository.findById(ticket.get().getId());
        assertThat(savedTicket).usingRecursiveComparison().isEqualTo(ticket);
    }

    @Test
    public void whenSaveSeveralTicketsThenGetAll() {
        Optional<Ticket> ticket1Opt = sql2oTicketRepository.save(new Ticket(1, 1, 2, 3, 1));
        Optional<Ticket> ticket2Opt = sql2oTicketRepository.save(new Ticket(2, 2, 2, 5, 2));
        Ticket ticket1 = ticket1Opt.orElseThrow();
        Ticket ticket2 = ticket2Opt.orElseThrow();
        var result = sql2oTicketRepository.findAll();
        assertThat(result).containsExactlyInAnyOrder(ticket1, ticket2);
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        assertThat(sql2oTicketRepository.findAll()).isEqualTo(emptyList());
        assertThat(sql2oTicketRepository.findById(0)).isEqualTo(empty());
    }
}
