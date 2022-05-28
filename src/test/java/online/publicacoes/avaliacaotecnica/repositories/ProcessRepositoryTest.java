package online.publicacoes.avaliacaotecnica.repositories;

import online.publicacoes.avaliacaotecnica.entities.Process;
import online.publicacoes.avaliacaotecnica.fixturies.ProcessFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class ProcessRepositoryTest {

  @Autowired ProcessRepository repository;

  Process process;

  @BeforeEach
  void setUp() {

    process = ProcessFixture.getProcess();
  }

  @Nested
  class ExistsByNumber {

    @Test
    @DisplayName("Process exists")
    void existsByNumberShouldReturnTrueWhenprocessExists() {

      repository.save(process);

      assertTrue(repository.existsByNumber(process.getNumber()));
    }

    @Test
    @DisplayName("Process does not exists")
    void existsByNumberShouldReturnFalseWhenProcessDoesNotExists() {

      assertFalse(repository.existsByNumber(process.getNumber()));
    }
  }

  @Nested
  class FindByName {

    @Test
    @DisplayName("Process exists")
    void findByNumberShouldReturnTheProcessWhenItExists() {

      repository.save(process);

      assertTrue(repository.findByNumber(process.getNumber()).isPresent());
    }

    @Test
    @DisplayName("Process does not exists")
    void findByNumberShouldReturnAnEmptyOptionalWhenProcessDoesNotExists() {

      assertTrue(repository.findByNumber(process.getNumber()).isEmpty());
    }
  }

  @Nested
  class DeleteByNumber {

    @Test
    void deleteByNumberShouldDeleteProcess() {

      Long id = repository.save(process).getId();

      repository.deleteByNumber(process.getNumber());

      assertTrue(repository.findById(id).isEmpty());
    }
  }
}
