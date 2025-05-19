package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.User;

import java.util.Optional;
import java.util.Properties;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class Sql2oUserRepositoryTest {

    private static Sql2oUserRepository sql2oUserRepository;

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oUserRepositoryTest.class.getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }

        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        var sql2o = configuration.databaseClient(datasource);

        sql2oUserRepository = new Sql2oUserRepository(sql2o);
    }

    @AfterEach
    public void clearUsers() {
        var users = sql2oUserRepository.findAll();
        for (var user : users) {
            sql2oUserRepository.deleteById(user.getId());
        }
    }

    @Test
    public void whenSaveAndFindByEmailAndPassword() {
        var user = sql2oUserRepository.save(new User(1, "Ivan Novikov", "novikov@mail.ru", "123")).get();
        var savedUser = sql2oUserRepository.findByEmailAndPassword("novikov@mail.ru", "123").get();
        assertThat(savedUser).usingRecursiveComparison().isEqualTo(user);
    }

    @Test
    public void whenSaveAndFindSeveralUsers() {
        var user1 = sql2oUserRepository.save(new User(2, "Vasiliy Sidorov", "sidorov@mail.ru", "sid789")).get();
        var user2 = sql2oUserRepository.save(new User(3, "Maxim Kuznetsov", "kuznetsov@mail.ru", "ku654")).get();
        var savedUser1 = sql2oUserRepository.findByEmailAndPassword("sidorov@mail.ru", "sid789").get();
        var savedUser2 = sql2oUserRepository.findByEmailAndPassword("kuznetsov@mail.ru", "ku654").get();
        assertThat(savedUser1).usingRecursiveComparison().isEqualTo(user1);
        assertThat(savedUser2).usingRecursiveComparison().isEqualTo(user2);
    }

    @Test
    public void whenSaveExistingEmail() {
        sql2oUserRepository.save(new User(6, "Nikolay Semenov", "semenov@mail.ru", "123")).get();
        var savedUser = sql2oUserRepository.save(
                new User(7, "Anatoliy Semenov", "semenov@mail.ru", "s789")
        );
        assertThat(savedUser).isEqualTo(Optional.empty());
    }
}
