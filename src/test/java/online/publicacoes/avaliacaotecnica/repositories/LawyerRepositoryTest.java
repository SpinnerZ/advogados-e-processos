package online.publicacoes.avaliacaotecnica.repositories;

import online.publicacoes.avaliacaotecnica.entities.Lawyer;
import online.publicacoes.avaliacaotecnica.fixturies.LawyerFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class LawyerRepositoryTest {

  @Autowired LawyerRepository repository;

  Lawyer lawyer;

  @BeforeEach
  void setUp() {

    lawyer = LawyerFixture.getLawyer();
  }

  @Nested
  class ExistsByUsername {

    @Test
    @DisplayName("Lawyer exists")
    void existsByUsernameShouldReturnTrueWhenLawyerExists() {

      repository.save(lawyer);

      assertTrue(repository.existsByUsername(lawyer.getUsername()));
    }

    @Test
    @DisplayName("Lawyer does not exists")
    void existsByUsernameShouldReturnFalseWhenLawyerDoesNotExists() {

      assertFalse(repository.existsByUsername(lawyer.getUsername()));
    }
  }

  @Nested
  class FindByUsername {

    @Test
    @DisplayName("Lawyer exists")
    void findByUsernameShouldReturnTheLawyerWhenItExists() {

      repository.save(lawyer);

      assertTrue(repository.findByUsername(lawyer.getUsername()).isPresent());
    }

    @Test
    @DisplayName("Lawyer does not exists")
    void findByUsernameShouldReturnAnEmptyOptionalWhenLawyerDoesNotExists() {

      assertTrue(repository.findByUsername(lawyer.getUsername()).isEmpty());
    }
  }

  @Nested
  class DeleteByUsername {

    @Test
    void deleteByUsernameShouldDeleteLawyer() {

      Long id = repository.save(lawyer).getId();

      repository.deleteByUsername(lawyer.getUsername());

      assertTrue(repository.findById(id).isEmpty());
    }
  }
}
