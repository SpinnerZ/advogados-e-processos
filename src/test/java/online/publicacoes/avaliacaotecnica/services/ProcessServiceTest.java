package online.publicacoes.avaliacaotecnica.services;

import online.publicacoes.avaliacaotecnica.entities.Lawyer;
import online.publicacoes.avaliacaotecnica.entities.Process;
import online.publicacoes.avaliacaotecnica.fixturies.LawyerFixture;
import online.publicacoes.avaliacaotecnica.fixturies.ProcessFixture;
import online.publicacoes.avaliacaotecnica.repositories.ProcessRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class ProcessServiceTest {

  @InjectMocks ProcessService service;
  @Mock ProcessRepository repository;
  @Mock LawyerService lawyerService;
  Process process;
  Lawyer lawyer;

  @BeforeEach
  void setUp() {

    process = ProcessFixture.getProcess();
    lawyer = LawyerFixture.getLawyer();

    when(repository.save(any())).then(returnsFirstArg());
  }

  @Nested
  class Create {

    @Test
    @DisplayName("Lawyer exists and the process does not exists")
    void createShouldCreateAProcessWhenLawyerExistsAndProcessDoesNotExists() {

      when(lawyerService.retrieve(lawyer.getUsername())).thenReturn(Optional.of(lawyer.toDTO()));
    }

    @Test
    @DisplayName("Lawyer exists and the process already exists")
    void createShouldThrowProcessAlreadyExistsExceptionWhenProcessAlreadyExists() {}

    @Test
    @DisplayName("Lawyer does not exists")
    void createShouldThrowLawyerNotFoundExceptionWhenLawyerDoesNotExists() {}

    @Test
    @DisplayName("All lawyer exists and none process exists")
    void createShouldCreateAllProcessWhenAllLawyersExistsAndNoneProcessExists() {}

    @Test
    @DisplayName("Some lawyers exists and some processes already exists")
    void createShouldCreateAllPossibleNewProcessAndReturnAListWithTheOnesWhoCant() {}
  }

  @Nested
  class Retrieve {

    @Test
    @DisplayName("All processes")
    void retrieveShouldReturnAllProcesses() {}

    @Test
    @DisplayName("Process exists")
    void retrieveShouldReturnTheExistentProcess() {}

    @Test
    @DisplayName("Process does not exists")
    void retrieveShouldThrowProcessNotFoundExceptionWhenProcessoIsNotFound() {}

    @Test
    @DisplayName("Filter: lawyer exists")
    void retrieveShouldReturnAllLawyerProcessesWhenLaywerExists() {}

    @Test
    @DisplayName("Filter: lawyer does no exists")
    void retrieveShouldThrowLawyerNotFoundExceptionWhenLawyerDoesNotExists() {}

    @Test
    @DisplayName("Filter: archived")
    void retrieveShouldReturnAllArchivedProcesses() {}

    @Test
    @DisplayName("Filter: not archived")
    void retrieveShouldReturnAllActiveProcesses() {}

    @Test
    @DisplayName("Filter: lawyer and not archived")
    void retriveShouldReturnAllProcessesWhoMatchesBothFilters() {}
  }

  @Nested
  class Update {

    @Test
    @DisplayName("Process exists")
    void updateShouldSaveNewDefendantWhenProcessExists() {}

    @Test
    @DisplayName("Process does not exists")
    void updateShouldCreateNewProcessWHenProcessIsNotFound() {}

    @Test
    @DisplayName("New username exists and process exists")
    void updateShouldUpdateProcessWithAllNewContent() {}

    @Test
    @DisplayName("New username exists and process does not exists")
    void updateShouldThrowProcessNotFoundExceptionWhenProcessIsNotFound() {}

    @Test
    @DisplayName("New username does not exists")
    void updateShouldThrowLawyerNotFoundExceptionWhenNewLawyerIsNotFound() {}
  }

  @Nested
  class Delete {

    @Test
    @DisplayName("Delete")
    void deleteShouldDeleteProcess() {}
  }
}
