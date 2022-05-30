package online.publicacoes.avaliacaotecnica.repositories;

import online.publicacoes.avaliacaotecnica.entities.Process;
import online.publicacoes.avaliacaotecnica.fixturies.ProcessFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class ProcessRepositoryTest {

  @Autowired ProcessRepository repository;
  @Autowired LawyerRepository lawyerRepository;

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

      lawyerRepository.save(process.getLawyer());
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

      lawyerRepository.save(process.getLawyer());
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

      lawyerRepository.save(process.getLawyer());
      Long id = repository.save(process).getId();

      repository.deleteByNumber(process.getNumber());

      assertTrue(repository.findById(id).isEmpty());
    }
  }

  @Nested
  class FindAllByLawyerAndArchived {

    @Test
    void findAllByLawyerAndArchivedShouldReturnAListOfProcessesAccordingFilter() {

      Process process1 = ProcessFixture.getProcess(1L);
      Process process2 = ProcessFixture.getProcess(2L);
      process2.setLawyer(process1.getLawyer());
      process2.setArchived(Boolean.TRUE);
      Process process3 = ProcessFixture.getProcess(3L);

      lawyerRepository.save(process1.getLawyer());
      lawyerRepository.save(process3.getLawyer());
      repository.save(process1);
      repository.save(process2);
      repository.save(process3);

      List<Process> result =
          repository.findAllByLawyerAndArchived(process1.getLawyer(), Boolean.TRUE);

      assertEquals(1, result.size());
    }

    @Test
    void findAllByLawyerAndArchivedShouldReturnAllProcessesFromOneLawyer() {

      Process process1 = ProcessFixture.getProcess(1L);
      Process process2 = ProcessFixture.getProcess(2L);
      Process process3 = ProcessFixture.getProcess(3L);
      process3.setLawyer(process1.getLawyer());

      lawyerRepository.save(process1.getLawyer());
      lawyerRepository.save(process2.getLawyer());

      repository.save(process1);
      repository.save(process2);
      repository.save(process3);

      List<Process> result = repository.findAllByLawyerAndArchived(process1.getLawyer(), null);

      assertEquals(2, result.size());
    }

    @Test
    void findAllByLawyerAndArchivedShouldReturnAllProcessAccordingWithParameter() {

      Process process1 = ProcessFixture.getProcess(1L);
      Process process2 = ProcessFixture.getProcess(2L);
      process2.setArchived(Boolean.TRUE);

      lawyerRepository.save(process1.getLawyer());
      lawyerRepository.save(process2.getLawyer());
      repository.save(process1);
      repository.save(process2);

      List<Process> actives = repository.findAllByLawyerAndArchived(null, Boolean.FALSE);
      List<Process> archived = repository.findAllByLawyerAndArchived(null, Boolean.TRUE);

      assertAll(() -> assertEquals(1, actives.size()), () -> assertEquals(1, archived.size()));
    }
  }
}
