package online.publicacoes.avaliacaotecnica.repositories;

import online.publicacoes.avaliacaotecnica.entities.Attorney;
import online.publicacoes.avaliacaotecnica.factories.AttorneyFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class AttorneyRepositoryTest {

  @Autowired AttorneyRepository repository;

  Attorney attorney;

  @BeforeEach
  void setUp() {

    attorney = AttorneyFactory.getAttorney();
  }

  @Test
  @DisplayName("When Attorney username already exists")
  void findByUserNameShouldReturnAnAttorneyWhenUsernameAlreadyExists() {

    repository.save(attorney);

    assertTrue(
        repository.findByUserName(attorney.getUsername()).isPresent(),
        "The repository didn't find a Attorney that already exists");
  }

  @Test
  @DisplayName("When Attorney username does not exists")
  void findByUserNameShouldReturnAnEmptyOptionalWhenUsernameDoesNotExists() {

    assertTrue(
        repository.findByUserName(attorney.getUsername()).isEmpty(),
        "The repository found an username who shouldn't exist");
  }
}
