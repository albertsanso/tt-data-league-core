package org.cttelsamicsterrassa.data.core.repository.jpa.practicioner.impl;

import org.cttelsamicsterrassa.data.core.domain.model.Practicioner;
import org.cttelsamicsterrassa.data.core.repository.jpa.practicioner.mapper.PracticionerJPAToPracticionerMapper;
import org.cttelsamicsterrassa.data.core.repository.jpa.practicioner.mapper.PracticionerToPracticionerJPAMapper;
import org.cttelsamicsterrassa.data.core.repository.jpa.practicioner.model.PracticionerJPA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = PracticionerRepositoryImplTest.TestApplication.class)
@Import({
		PracticionerRepositoryImpl.class,
		PracticionerJPAToPracticionerMapper.class,
		PracticionerToPracticionerJPAMapper.class
})
class PracticionerRepositoryImplTest {

	@SpringBootConfiguration
	@EnableAutoConfiguration
	@EntityScan(basePackages = "org.cttelsamicsterrassa.data.core.repository.jpa")
	@EnableJpaRepositories(basePackages = "org.cttelsamicsterrassa.data.core.repository.jpa")
	static class TestApplication {
	}

	@Autowired
	private PracticionerRepositoryImpl repository;

	@Autowired
	private PracticionerRepositoryHelper practicionerRepositoryHelper;

	@BeforeEach
	void setUp() {
		practicionerRepositoryHelper.deleteAll();
	}

	@Test
	void searchBySimilarNameSingleFragmentContainsIgnoreCaseReturnsMatches() {
		Practicioner johnSmith = persistPracticioner("John", "Smith", "John Smith");
		Practicioner anaSmithe = persistPracticioner("Ana", "Smithe", "Smithe, Ana");
		persistPracticioner("Alice", "Brown", "Alice Brown");

		List<Practicioner> result = repository.searchBySimilarName("sMi");

		Set<UUID> resultIds = result.stream().map(Practicioner::getId).collect(Collectors.toSet());
		assertEquals(2, result.size());
		assertTrue(resultIds.contains(johnSmith.getId()));
		assertTrue(resultIds.contains(anaSmithe.getId()));
	}

	@Test
	void searchBySimilarNameMultiFragmentExtraSpacesNormalizesAndMatches() {
		Practicioner johnSmith = persistPracticioner("John", "Smith", "John Smith");
		Practicioner smiJohn = persistPracticioner("Smi", "John", "Smi John");
		persistPracticioner("John", "Carter", "John Carter");

		List<Practicioner> result = repository.searchBySimilarName("  john   smi  ");

		Set<UUID> resultIds = result.stream().map(Practicioner::getId).collect(Collectors.toSet());
		assertEquals(2, result.size());
		assertTrue(resultIds.contains(johnSmith.getId()));
		assertTrue(resultIds.contains(smiJohn.getId()));
	}

	@Test
	void searchBySimilarNameMultiFragmentEnforcesAndSemanticsExcludesPartialMatches() {
		Practicioner johnSmith = persistPracticioner("John", "Smith", "John Smith");
		persistPracticioner("John", "Carter", "John Carter");
		persistPracticioner("Alice", "Smith", "Alice Smith");

		List<Practicioner> result = repository.searchBySimilarName("john smi");

		assertEquals(1, result.size());
		assertEquals(johnSmith.getId(), result.getFirst().getId());
	}

	@Test
	void searchBySimilarNameNoMatchesReturnsEmptyList() {
		persistPracticioner("John", "Smith", "John Smith");

		List<Practicioner> result = repository.searchBySimilarName("non-existent");

		assertEquals(0, result.size());
	}

	@Test
	void searchBySimilarNameNullQueryThrowsIllegalArgumentException() {
		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> repository.searchBySimilarName(null)
		);

		assertEquals("Practicioner name fragment cannot be null or blank", exception.getMessage());
	}

	@Test
	void searchBySimilarNameBlankQueryThrowsIllegalArgumentException() {
		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> repository.searchBySimilarName("   ")
		);

		assertEquals("Practicioner name fragment cannot be null or blank", exception.getMessage());
	}

	private Practicioner persistPracticioner(String firstName, String secondName, String fullName) {
		PracticionerJPA practicionerJPA = new PracticionerJPA();
		practicionerJPA.setId(UUID.randomUUID());
		practicionerJPA.setFirstName(firstName);
		practicionerJPA.setSecondName(secondName);
		practicionerJPA.setFullName(fullName);
		practicionerJPA.setBirthDate(new Date());
		practicionerRepositoryHelper.saveAndFlush(practicionerJPA);

		return Practicioner.createExisting(
				practicionerJPA.getId(),
				firstName,
				secondName,
				fullName,
				practicionerJPA.getBirthDate()
		);
	}
}

