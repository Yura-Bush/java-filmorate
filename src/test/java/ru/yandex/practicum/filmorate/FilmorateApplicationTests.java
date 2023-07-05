package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class FilmorateApplicationTests {

	private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	private Film getNewFilm() {
		Film film = new Film();
		film.setName("Зеленая Миля");
		film.setDescription("В тюрьме для смертников появляется заключенный с божественным даром. Мистическая драма по роману Стивена Кинга");
		film.setReleaseDate(LocalDate.of(1999, 12, 6));
		film.setDuration(189);
		return film;
	}

	@Test
	void shouldPassValidationWithValidFilm() {
		Film film = getNewFilm();

		Set<ConstraintViolation<Film>> violations = validator.validate(film);
		assertEquals(0, violations.size());
	}

	@Test
	void shouldNotPassValidationIfFilmTitleIsBlank() {
		Film film = getNewFilm();
		film.setName("");

		Set<ConstraintViolation<Film>> violations = validator.validate(film);
		List<String> errorMessage = violations.stream()
				.map(ConstraintViolation::getMessage)
				.collect(Collectors.toList());

		assertEquals(1, violations.size());
		assertEquals("Название не может быть пустым", errorMessage.get(0));
	}

	@Test
	void shouldNotPassValidationIfDescriptionIsMoreThan200Symbols() {
		Film film = getNewFilm();

		film.setDescription("!".repeat(201));

		Set<ConstraintViolation<Film>> violations = validator.validate(film);
		List<String> errorMessage = violations.stream()
				.map(ConstraintViolation::getMessage)
				.collect(Collectors.toList());

		assertEquals(1, violations.size());
		assertEquals("Описание не должно быть больше 200 символов", errorMessage.get(0));
	}

	@Test
	void shouldNotPassValidationIfDateIsLessThanFilmIndustryStarted() {
		LocalDate filmIndustryStart = LocalDate.of(1895, 12, 28);

		Film film = getNewFilm();

		film.setReleaseDate(LocalDate.of(1895, 11, 28));

		Set<ConstraintViolation<Film>> violations = validator.validate(film);

		List<String> errorMessage = violations.stream()
				.map(ConstraintViolation::getMessage)
				.collect(Collectors.toList());

		assertEquals(1, violations.size());
		assertEquals("Дата не может быть раньше, чем 28.12.1985", errorMessage.get(0));
	}
	private User getNewUser() {
		User user = new User();
		user.setEmail("example@yandex.ru");
		user.setLogin("ЮзерЛогин");
		user.setName("Евгений Бушуев");
		user.setBirthday(LocalDate.of(1994, 8, 30));
		return user;
	}

	@Test
	void shouldPassValidationWithValidUser() {
		User user = getNewUser();

		Set<ConstraintViolation<User>> violations = validator.validate(user);
		assertEquals(0, violations.size());
	}

	@Test
	void shouldNotPassValidationIfUserEmailIsIncorrect() {
		User user = getNewUser();
		user.setEmail("wrongEmail.com");

		Set<ConstraintViolation<User>> violations = validator.validate(user);
		List<String> errorMessage = violations.stream()
				.map(ConstraintViolation::getMessage)
				.collect(Collectors.toList());

		assertEquals(1, violations.size());
		assertEquals("Email должен быть корректный xxx@example.com", errorMessage.get(0));
	}

	@Test
	void shouldNotPassValidationIfLoginIsBlanc() {
		User user = getNewUser();
		user.setLogin("");

		Set<ConstraintViolation<User>> violations = validator.validate(user);
		List<String> errorMessage1 = violations.stream()
				.map(ConstraintViolation::getMessage)
				.collect(Collectors.toList());

		assertEquals(1, violations.size());
		assertEquals("Логин не может быть пустым", errorMessage1.get(0));

	}

	@Test
	void shouldNotPassValidationIfLoginContainsSpaces() {
		User user = getNewUser();

		user.setLogin("User Login");

		Set<ConstraintViolation<User>> violations = validator.validate(user);
		List<String> errorMessage = violations.stream()
				.map(ConstraintViolation::getMessage)
				.collect(Collectors.toList());

		assertEquals("Логин не может содержать пробелы", errorMessage.get(0));
		assertEquals(1, violations.size());
	}

	@Test
	void shouldNotPassValidationIfUserBirthDateIsAfterThanCurrentTime() {
		User user = getNewUser();
		user.setBirthday(LocalDate.MAX);

		Set<ConstraintViolation<User>> violations = validator.validate(user);
		List<String> errorMessage = violations.stream()
				.map(ConstraintViolation::getMessage)
				.collect(Collectors.toList());

		assertEquals(1, violations.size());
		assertEquals("Дата рождения не может быть в будущем времени", errorMessage.get(0));
	}
}
