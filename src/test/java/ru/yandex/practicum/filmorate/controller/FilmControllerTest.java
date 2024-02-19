package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmControllerTest {
    private Validator validator;
    private final FilmController controller;

    @BeforeEach
    public void beforeEach() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void createFilm() {
        Film film = Film.builder().name("Титаник").description("О крушении")
                .releaseDate(LocalDate.of(1990, 1, 1)).duration(300L).mpa(Mpa.G)
                .likes(Set.of()).genres(new LinkedHashSet<>() {{
                    add(Genre.CARTOON);
                    add(Genre.ACTION);
                }}).build();
        controller.addFilm(film);

        Assertions.assertEquals(1, controller.getFilms().size(), "Количество фильмов не совпадает.");
        Assertions.assertEquals(film.getName(), controller.getFilms().get(0).getName(),
                "Название фильмов не совпадает.");
        Assertions.assertEquals(film.getDescription(), controller.getFilms().get(0).getDescription(),
                "Описание фильмов не совпадает.");
        Assertions.assertEquals(film.getReleaseDate(), controller.getFilms().get(0).getReleaseDate(),
                "Дата выхода фильмов не совпадает.");
        Assertions.assertEquals(film.getDuration(), controller.getFilms().get(0).getDuration(),
                "Длительность фильмов не совпадает.");
        Assertions.assertEquals(film.getMpa(), controller.getFilms().get(0).getMpa(),
                "Рейтинг MPA фильмов не совпадает.");
        Assertions.assertEquals(film.getLikes(), controller.getFilms().get(0).getLikes(),
                "Количество лайков фильмов не совпадает.");
        Assertions.assertEquals(film.getGenres(), controller.getFilms().get(0).getGenres(),
                "Жанры фильмов не совпадают.");
    }

    @Test
    void createFilmWithBlankName() {
        Film film = Film.builder().name(" ").description("О крушении")
                .releaseDate(LocalDate.of(1990, 1, 1)).duration(300L).mpa(Mpa.G)
                .likes(Set.of())
                .genres(new LinkedHashSet<>() {{
                    add(Genre.CARTOON);
                    add(Genre.ACTION);
                }}).build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    void createFilmWithDescriptionLengthMoreThan200() {
        Film film = Film.builder().name("Титаник").description("А".repeat(777))
                .releaseDate(LocalDate.of(1990, 1, 1)).duration(300L).mpa(Mpa.G)
                .likes(Set.of())
                .genres(new LinkedHashSet<>() {{
                    add(Genre.CARTOON);
                    add(Genre.ACTION);
                }}).build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    void createFilmWithReleaseDateBefore28DecemberOf1985() {
        Film film = Film.builder().name("Титаник").description("О крушении")
                .releaseDate(LocalDate.of(1800, 1, 1)).duration(300L).mpa(Mpa.G)
                .likes(Set.of())
                .genres(new LinkedHashSet<>() {{
                    add(Genre.CARTOON);
                    add(Genre.ACTION);
                }}).build();

        ValidationException e = Assertions.assertThrows(ValidationException.class, () -> controller.addFilm(film));
        Assertions.assertEquals("Дата релиза не должна быть раньше 28 декабря 1895 года. Введено: "
                + film.getReleaseDate(), e.getMessage(), "Сообщения об ошибке не совпадают.");
    }

    @Test
    void createFilmWithNotPositiveDuration() {
        Film film = Film.builder().name("Титаник").description("О крушении")
                .releaseDate(LocalDate.of(1990, 1, 1)).duration(-300L).mpa(Mpa.G)
                .likes(Set.of())
                .genres(new LinkedHashSet<>() {{
                    add(Genre.CARTOON);
                    add(Genre.ACTION);
                }}).build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    void createFilmWithEmptyFilmOrWithoutNameOrDescriptionOrReleaseDateOrDuration() {
        Film filmEmpty = Film.builder().build();
        Film filmWithoutName = Film.builder().description("О крушении")
                .releaseDate(LocalDate.of(1990, 1, 1)).duration(300L).mpa(Mpa.G)
                .likes(Set.of()).genres(new LinkedHashSet<>() {{
                    add(Genre.CARTOON);
                    add(Genre.ACTION);
                }}).build();
        Film filmWithoutDescription = Film.builder().name("Титаник")
                .releaseDate(LocalDate.of(1990, 1, 1)).duration(300L).mpa(Mpa.G)
                .likes(Set.of()).genres(new LinkedHashSet<>() {{
                    add(Genre.CARTOON);
                    add(Genre.ACTION);
                }}).build();
        Film filmWithoutReleaseDate = Film.builder().name("Титаник").description("О крушении")
                .duration(300L).mpa(Mpa.G)
                .likes(Set.of()).genres(new LinkedHashSet<>() {{
                    add(Genre.CARTOON);
                    add(Genre.ACTION);
                }}).build();
        Film filmWithoutDuration = Film.builder().name("Титаник").description("О крушении")
                .releaseDate(LocalDate.of(1990, 1, 1)).mpa(Mpa.G)
                .likes(Set.of()).genres(new LinkedHashSet<>() {{
                    add(Genre.CARTOON);
                    add(Genre.ACTION);
                }}).build();
        Film filmWithoutMpa = Film.builder().name("Титаник").description("О крушении")
                .releaseDate(LocalDate.of(1990, 1, 1))
                .likes(Set.of()).genres(new LinkedHashSet<>() {{
                    add(Genre.CARTOON);
                    add(Genre.ACTION);
                }}).build();

        Set<ConstraintViolation<Film>> violations = validator.validate(filmEmpty);
        Assertions.assertFalse(violations.isEmpty());
        Set<ConstraintViolation<Film>> violations2 = validator.validate(filmWithoutName);
        Assertions.assertFalse(violations2.isEmpty());
        Set<ConstraintViolation<Film>> violations3 = validator.validate(filmWithoutDescription);
        Assertions.assertFalse(violations3.isEmpty());
        Set<ConstraintViolation<Film>> violations4 = validator.validate(filmWithoutReleaseDate);
        Assertions.assertFalse(violations4.isEmpty());
        Set<ConstraintViolation<Film>> violations5 = validator.validate(filmWithoutDuration);
        Assertions.assertFalse(violations5.isEmpty());
        Set<ConstraintViolation<Film>> violations6 = validator.validate(filmWithoutMpa);
        Assertions.assertFalse(violations6.isEmpty());
    }

    @Test
    void updateFilm() {
        Film film = Film.builder().name("Титаник").description("О крушении")
                .releaseDate(LocalDate.of(1990, 1, 1)).duration(300L).mpa(Mpa.G)
                .likes(Set.of()).genres(new LinkedHashSet<>() {{
                    add(Genre.CARTOON);
                    add(Genre.ACTION);
                }}).build();
        Film addedFilm = controller.addFilm(film);

        Film updatedFilm = film.toBuilder().id(addedFilm.getId()).name("Лайнер").description("О восстановлении")
                .releaseDate(LocalDate.of(2000, 2, 2)).duration(400L).mpa(Mpa.PG)
                .likes(Set.of()).genres(new LinkedHashSet<>() {{
                    add(Genre.DRAMA);
                    add(Genre.COMEDY);
                }}).build();
        controller.updateFilm(updatedFilm);

        Assertions.assertEquals(1, controller.getFilms().size(), "Количество фильмов не совпадает.");
        Assertions.assertEquals(updatedFilm, controller.getFilms().get(0), "Фильмы не совпадают.");
    }

    @Test
    void updateFilmWithBlankName() {
        Film film = Film.builder().name("Титаник").description("О крушении")
                .releaseDate(LocalDate.of(1990, 1, 1)).duration(300L).mpa(Mpa.G)
                .likes(Set.of()).genres(new LinkedHashSet<>() {{
                    add(Genre.CARTOON);
                    add(Genre.ACTION);
                }}).build();
        Film addedFilm = controller.addFilm(film);
        Film updatedFilm = film.toBuilder().id(addedFilm.getId()).name(" ").build();

        Set<ConstraintViolation<Film>> violations = validator.validate(updatedFilm);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    void updateFilmWithDescriptionLengthMoreThan200() {
        Film film = Film.builder().name("Титаник").description("О крушении")
                .releaseDate(LocalDate.of(1990, 1, 1)).duration(300L).mpa(Mpa.G)
                .likes(Set.of()).genres(new LinkedHashSet<>() {{
                    add(Genre.CARTOON);
                    add(Genre.ACTION);
                }}).build();
        Film addedFilm = controller.addFilm(film);
        Film updatedFilm = film.toBuilder().id(addedFilm.getId()).description("A".repeat(777)).build();

        Set<ConstraintViolation<Film>> violations = validator.validate(updatedFilm);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    void updateFilmWithReleaseDateBefore28DecemberOf1985() {
        Film film = Film.builder().name("Титаник").description("О крушении")
                .releaseDate(LocalDate.of(1990, 1, 1)).duration(300L).mpa(Mpa.G)
                .likes(Set.of()).genres(new LinkedHashSet<>() {{
                    add(Genre.CARTOON);
                    add(Genre.ACTION);
                }}).build();
        Film addedFilm = controller.addFilm(film);
        Film updatedFilm = film.toBuilder().id(addedFilm.getId())
                .releaseDate(LocalDate.of(1700, 1, 1)).build();

        ValidationException e = Assertions.assertThrows(ValidationException.class,
                () -> controller.updateFilm(updatedFilm));
        Assertions.assertEquals("Дата релиза не должна быть раньше 28 декабря 1895 года. Введено: "
                + updatedFilm.getReleaseDate(), e.getMessage(), "Сообщения об ошибке не совпадают.");
    }

    @Test
    void updateFilmWithNotPositiveDuration() {
        Film film = Film.builder().name("Титаник").description("О крушении")
                .releaseDate(LocalDate.of(1990, 1, 1)).duration(300L).mpa(Mpa.G)
                .likes(Set.of()).genres(new LinkedHashSet<>() {{
                    add(Genre.CARTOON);
                    add(Genre.ACTION);
                }}).build();
        controller.addFilm(film);
        Film updatedFilm = film.toBuilder().duration(-100L).build();
        Film updatedFilm2 = film.toBuilder().duration(0L).build();

        Set<ConstraintViolation<Film>> violations = validator.validate(updatedFilm);
        Assertions.assertFalse(violations.isEmpty());
        Set<ConstraintViolation<Film>> violations2 = validator.validate(updatedFilm2);
        Assertions.assertFalse(violations2.isEmpty());
    }

    @Test
    void updateFilmWithNonexistentId() {
        Film film = Film.builder().name("Титаник").description("О крушении")
                .releaseDate(LocalDate.of(1990, 1, 1)).duration(300L).mpa(Mpa.G)
                .likes(Set.of()).genres(new LinkedHashSet<>() {{
                    add(Genre.CARTOON);
                    add(Genre.ACTION);
                }}).build();
        controller.addFilm(film);
        Film updatedFilm = film.toBuilder().id(9999).build();

        FilmNotFoundException e = Assertions.assertThrows(FilmNotFoundException.class,
                () -> controller.updateFilm(updatedFilm));
        Assertions.assertEquals("Фильма с id " + updatedFilm.getId() + " не существует.",
                e.getMessage(), "Сообщения об ошибке не совпадают.");
    }


    @Test
    void updateFilmWithEmptyFilmOrWithoutNameOrDescriptionOrReleaseDateOrDuration() {
        Film film = Film.builder().name("Титаник").description("О крушении")
                .releaseDate(LocalDate.of(1990, 1, 1)).duration(300L).mpa(Mpa.G)
                .likes(Set.of()).genres(new LinkedHashSet<>() {{
                    add(Genre.CARTOON);
                    add(Genre.ACTION);
                }}).build();
        controller.addFilm(film);

        Film updatedFilmEmpty = Film.builder().id(film.getId()).build();
        Film updatedFilmWithoutName = Film.builder().id(film.getId()).description("О крушении")
                .releaseDate(LocalDate.of(1990, 1, 1)).duration(300L).build();
        Film updatedFilmWithoutDescription = Film.builder().id(film.getId()).name("Титаник")
                .releaseDate(LocalDate.of(1990, 1, 1)).duration(300L).build();
        Film updatedFilmWithoutReleaseDate = Film.builder().id(film.getId()).name("Титаник").description("О крушении")
                .duration(300L).build();
        Film updatedFilmWithoutDuration = Film.builder().id(film.getId()).name("Титаник").description("О крушении")
                .releaseDate(LocalDate.of(1990, 1, 1)).build();

        Set<ConstraintViolation<Film>> violations = validator.validate(updatedFilmEmpty);
        Assertions.assertFalse(violations.isEmpty());
        Set<ConstraintViolation<Film>> violations2 = validator.validate(updatedFilmWithoutName);
        Assertions.assertFalse(violations2.isEmpty());
        Set<ConstraintViolation<Film>> violations3 = validator.validate(updatedFilmWithoutDescription);
        Assertions.assertFalse(violations3.isEmpty());
        Set<ConstraintViolation<Film>> violations4 = validator.validate(updatedFilmWithoutReleaseDate);
        Assertions.assertFalse(violations4.isEmpty());
        Set<ConstraintViolation<Film>> violations5 = validator.validate(updatedFilmWithoutDuration);
        Assertions.assertFalse(violations5.isEmpty());
    }
}