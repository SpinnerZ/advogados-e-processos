package online.publicacoes.avaliacaotecnica.repositories;

import online.publicacoes.avaliacaotecnica.entities.Lawyer;
import online.publicacoes.avaliacaotecnica.factories.LawyerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class LawyerRepositoryTest {

  @Autowired LawyerRepository repository;

  Lawyer lawyer;

  @BeforeEach
  void setUp() {

    lawyer = LawyerFactory.getLawyer();
  }

  @Nested
  @DisplayName("Find tests")
  class Find {

    @Test
    @DisplayName("Lawyer username already exists")
    void findByUserNameShouldReturnAnAttorneyWhenUsernameAlreadyExists() {

      repository.save(lawyer);

      assertTrue(
              repository.findByUsername(lawyer.getUsername()).isPresent(),
              "The repository didn't find a Lawyer that already exists");
    }

    @Test
    @DisplayName("Lawyer username does not exists")
    void findByUserNameShouldReturnAnEmptyOptionalWhenUsernameDoesNotExists() {

      assertTrue(
              repository.findByUsername(lawyer.getUsername()).isEmpty(),
              "The repository found an username who shouldn't exist");
    }
  }

  @Nested
  @DisplayName("Delete tests")
  class Delete {

    @Test
    @DisplayName("Delete Lawyer")
    void deleteByUsernameShouldDeleteLawyer() {

      repository.save(lawyer);

      repository.deleteByUsername(lawyer.getUsername());

      assertTrue(repository.findByUsername(lawyer.getUsername()).isEmpty());
    }
  }
}
