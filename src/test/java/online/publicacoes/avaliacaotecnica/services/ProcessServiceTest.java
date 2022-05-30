package online.publicacoes.avaliacaotecnica.services;

import online.publicacoes.avaliacaotecnica.dto.ProcessDTO;
import online.publicacoes.avaliacaotecnica.dto.ProcessFilterDTO;
import online.publicacoes.avaliacaotecnica.entities.Lawyer;
import online.publicacoes.avaliacaotecnica.entities.Process;
import online.publicacoes.avaliacaotecnica.exceptions.LawyerNotFoundException;
import online.publicacoes.avaliacaotecnica.exceptions.NotAllProcessesCreatedException;
import online.publicacoes.avaliacaotecnica.exceptions.ProcessAlreadyExistsException;
import online.publicacoes.avaliacaotecnica.exceptions.ProcessCreatedInsteadOfUpdatedException;
import online.publicacoes.avaliacaotecnica.exceptions.ProcessNotFoundException;
import online.publicacoes.avaliacaotecnica.fixturies.ProcessFixture;
import online.publicacoes.avaliacaotecnica.repositories.LawyerRepository;
import online.publicacoes.avaliacaotecnica.repositories.ProcessRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class ProcessServiceTest {

  @InjectMocks ProcessService service;
  @Mock ProcessRepository repository;
  @Mock LawyerRepository lawyerRepository;
  Process process;
  ProcessDTO requestDTO;
  Lawyer lawyer;

  @BeforeEach
  void setUp() {

    process = ProcessFixture.getProcess();
    lawyer = process.getLawyer();
    requestDTO = process.toDTO();

    when(repository.save(any())).then(returnsFirstArg());
  }

  @Nested
  class Create {

    @Test
    @DisplayName("Lawyer exists and the process does not exists")
    void createShouldCreateAProcessWhenLawyerExistsAndProcessDoesNotExists() {

      when(lawyerRepository.findByUsername(lawyer.getUsername())).thenReturn(Optional.of(lawyer));
      when(repository.existsByNumber(process.getNumber())).thenReturn(false);

      ProcessDTO result = service.create(requestDTO);

      verify(repository).existsByNumber(process.getNumber());
      verify(repository).save(process);
      assertEquals(requestDTO, result);
    }

    @Test
    @DisplayName("Lawyer exists and the process already exists")
    void createShouldThrowProcessAlreadyExistsExceptionWhenProcessAlreadyExists() {

      when(lawyerRepository.findByUsername(lawyer.getUsername())).thenReturn(Optional.of(lawyer));
      when(repository.existsByNumber(process.getNumber())).thenReturn(true);

      ProcessAlreadyExistsException exception =
          assertThrows(ProcessAlreadyExistsException.class, () -> service.create(process.toDTO()));
      assertEquals(process.getNumber(), exception.getNumber());
    }

    @Test
    @DisplayName("Lawyer does not exists")
    void createShouldThrowLawyerNotFoundExceptionWhenLawyerDoesNotExists() {

      when(lawyerRepository.findByUsername(lawyer.getUsername())).thenReturn(Optional.empty());

      LawyerNotFoundException exception =
          assertThrows(LawyerNotFoundException.class, (() -> service.create(requestDTO)));
      assertEquals(lawyer.getUsername(), exception.getUsername());
    }

    @Test
    @DisplayName("All lawyer exists and none process exists")
    void createShouldCreateAllProcessWhenAllLawyersExistsAndNoneProcessExists() {

      Process process1 = ProcessFixture.getProcess(1L);
      Process process2 = ProcessFixture.getProcess(2L);
      Process process3 = ProcessFixture.getProcess(3L);

      when(lawyerRepository.findByUsername(process1.getLawyer().getUsername()))
          .thenReturn(Optional.of(process1.getLawyer()));
      when(lawyerRepository.findByUsername(process2.getLawyer().getUsername()))
          .thenReturn(Optional.of(process2.getLawyer()));
      when(lawyerRepository.findByUsername(process3.getLawyer().getUsername()))
          .thenReturn(Optional.of(process3.getLawyer()));
      when(repository.existsByNumber(any())).thenReturn(false);

      Set<ProcessDTO> processDTOs = Set.of(process1.toDTO(), process2.toDTO(), process3.toDTO());

      List<ProcessDTO> resultList = service.create(processDTOs);

      assertEquals(3, resultList.size());
      verify(repository, times(3)).save(any());
      verify(repository, times(3)).existsByNumber(any());
      verify(lawyerRepository, times(3)).findByUsername(any());
    }

    @Test
    @DisplayName("Some lawyers exists and some processes already exists")
    void createShouldCreateAllPossibleNewProcessAndReturnAListWithTheOnesWhoCant() {

      Process process1 = ProcessFixture.getProcess(1L);
      when(lawyerRepository.findByUsername(process1.getLawyer().getUsername()))
          .thenReturn(Optional.of(process1.getLawyer()));
      when(repository.existsByNumber(process1.getNumber())).thenReturn(false);

      Process process2 = ProcessFixture.getProcess(2L);
      when(lawyerRepository.findByUsername(process2.getLawyer().getUsername()))
          .thenReturn(Optional.empty());
      when(repository.existsByNumber(process2.getNumber())).thenReturn(false);

      Process process3 = ProcessFixture.getProcess(3L);
      when(lawyerRepository.findByUsername(process3.getLawyer().getUsername()))
          .thenReturn(Optional.of(process3.getLawyer()));
      when(repository.existsByNumber(process3.getNumber())).thenReturn(true);

      Set<ProcessDTO> processDTOs = Set.of(process1.toDTO(), process2.toDTO(), process3.toDTO());

      NotAllProcessesCreatedException exception =
          assertThrows(NotAllProcessesCreatedException.class, () -> service.create(processDTOs));

      assertAll(
          () -> assertEquals(1, exception.getSuccess().size(), "Success count is wrong"),
          () -> assertEquals(2, exception.getFailure().size(), "Failure count is wrong"));
      verify(repository).save(any());
      verify(repository, times(2)).existsByNumber(any());
      verify(lawyerRepository, times(3)).findByUsername(any());
    }
  }

  @Nested
  class Retrieve {

    @Test
    @DisplayName("All processes")
    void retrieveShouldReturnAllProcesses() {

      when(repository.findAll()).thenReturn(new ArrayList<>());

      service.retrieve();

      verify(repository).findAll();
    }

    @Test
    @DisplayName("Process exists")
    void retrieveShouldReturnTheExistentProcess() {

      when(repository.findByNumber(process.getNumber())).thenReturn(Optional.of(process));

      Optional<ProcessDTO> result = service.retrieve(process.getNumber());

      assertTrue(result.isPresent());
      verify(repository).findByNumber(process.getNumber());
    }

    @Test
    @DisplayName("Process does not exists")
    void retrieveShouldReturnAnEmptyOptionalWhenProcessIsNotFound() {

      when(repository.findByNumber(process.getNumber())).thenReturn(Optional.empty());

      Optional<ProcessDTO> result = service.retrieve(process.getNumber());

      assertTrue(result.isEmpty());
      verify(repository).findByNumber(process.getNumber());
    }

    @Test
    @DisplayName("Filter: lawyer exists")
    void retrieveShouldReturnAllLawyerProcessesWhenLawyerExists() {

      when(lawyerRepository.findByUsername(lawyer.getUsername())).thenReturn(Optional.of(lawyer));
      when(repository.findAllByLawyerAndArchived(lawyer, null)).thenReturn(List.of(process));

      ProcessFilterDTO filter = ProcessFilterDTO.builder().username(lawyer.getUsername()).build();

      List<ProcessDTO> result = service.retrieve(filter);

      assertEquals(1, result.size());
      verify(lawyerRepository).findByUsername(lawyer.getUsername());
      verify(repository).findAllByLawyerAndArchived(lawyer, null);
    }

    @Test
    @DisplayName("Filter: lawyer does no exists")
    void retrieveShouldThrowLawyerNotFoundExceptionWhenLawyerDoesNotExists() {

      when(lawyerRepository.findByUsername(lawyer.getUsername())).thenReturn(Optional.empty());

      ProcessFilterDTO filter = ProcessFilterDTO.builder().username(lawyer.getUsername()).build();

      assertThrows(LawyerNotFoundException.class, () -> service.retrieve(filter));
    }

    @Test
    @DisplayName("Filter: archived")
    void retrieveShouldReturnAllArchivedProcesses() {

      when(repository.findAllByLawyerAndArchived(null, Boolean.TRUE)).thenReturn(List.of(process));

      ProcessFilterDTO filter = ProcessFilterDTO.builder().archived(Boolean.TRUE).build();

      List<ProcessDTO> result = service.retrieve(filter);

      assertEquals(1, result.size());
      verify(repository).findAllByLawyerAndArchived(null, Boolean.TRUE);
    }

    @Test
    @DisplayName("Filter: not archived")
    void retrieveShouldReturnAllActiveProcesses() {

      when(repository.findAllByLawyerAndArchived(null, Boolean.FALSE)).thenReturn(List.of(process));

      ProcessFilterDTO filter = ProcessFilterDTO.builder().archived(Boolean.FALSE).build();

      List<ProcessDTO> result = service.retrieve(filter);

      assertEquals(1, result.size());
      verify(repository).findAllByLawyerAndArchived(null, Boolean.FALSE);
    }

    @Test
    @DisplayName("Filter: lawyer and not archived")
    void retrieveShouldReturnAllProcessesWhoMatchesBothFilters() {

      when(lawyerRepository.findByUsername(lawyer.getUsername())).thenReturn(Optional.of(lawyer));
      when(repository.findAllByLawyerAndArchived(lawyer, Boolean.FALSE))
          .thenReturn(List.of(process));

      ProcessFilterDTO filter =
          ProcessFilterDTO.builder().username(lawyer.getUsername()).archived(Boolean.FALSE).build();

      List<ProcessDTO> result = service.retrieve(filter);

      assertEquals(1, result.size());
      verify(lawyerRepository).findByUsername(lawyer.getUsername());
      verify(repository).findAllByLawyerAndArchived(lawyer, Boolean.FALSE);
    }
  }

  @Nested
  class Update {

    @Test
    @DisplayName("Update defender when process exists")
    void updateShouldSaveNewDefendantWhenProcessExists() {

      when(repository.findByNumber(process.getNumber())).thenReturn(Optional.of(process));

      ProcessDTO result = service.update(process.getNumber(), "Dart Valadão");

      assertEquals("Dart Valadão", result.getDefendant());
      verify(repository).save(any());
    }

    @Test
    @DisplayName("Update defender when process does not exists")
    void updateShouldCreateNewProcessWHenProcessIsNotFound() {

      when(repository.findByNumber(process.getNumber())).thenReturn(Optional.empty());

      assertThrows(
          ProcessNotFoundException.class, () -> service.update(process.getNumber(), "Any name"));
    }

    @Test
    @DisplayName("Update everything when new username exists and process exists")
    void updateShouldUpdateProcessWithAllNewContent() {

      Process process2 = ProcessFixture.getProcess(2L);
      process2.setNumber(process.getNumber());

      when(lawyerRepository.findByUsername(process2.getLawyer().getUsername()))
          .thenReturn(Optional.of(process2.getLawyer()));
      when(repository.findByNumber(process.getNumber())).thenReturn(Optional.of(process));

      ProcessDTO result = service.update(process2.toDTO());

      assertAll(
          () -> assertEquals(process.getNumber(), result.getNumber()),
          () -> assertEquals(process2.getDefendant(), result.getDefendant()),
          () -> assertEquals(process2.getArchived(), result.getArchived()),
          () -> assertEquals(process2.getLawyer().getUsername(), result.getLawyerUsername()));
      verify(repository).save(process2);
    }

    @Test
    @DisplayName("Update everything when new username exists and process does not exists")
    void updateShouldThrowProcessNotFoundExceptionWhenProcessIsNotFound() {

      when(repository.findByNumber(process.getNumber())).thenReturn(Optional.empty());
      when(lawyerRepository.findByUsername(process.getLawyer().getUsername()))
          .thenReturn(Optional.of(process.getLawyer()));
      when(repository.existsByNumber(process.getNumber())).thenReturn(Boolean.FALSE);

      ProcessCreatedInsteadOfUpdatedException exception =
          assertThrows(
              ProcessCreatedInsteadOfUpdatedException.class, () -> service.update(process.toDTO()));

      ProcessDTO result = exception.getProcessDTO();

      assertAll(
          () -> assertEquals(process.getNumber(), result.getNumber()),
          () -> assertEquals(process.getDefendant(), result.getDefendant()),
          () -> assertEquals(process.getArchived(), result.getArchived()),
          () -> assertEquals(process.getLawyer().getUsername(), result.getLawyerUsername()));
      verify(repository).save(process);
    }

    @Test
    @DisplayName("Update everything when new username does not exists")
    void updateShouldThrowLawyerNotFoundExceptionWhenNewLawyerIsNotFound() {

      Process process2 = ProcessFixture.getProcess(2L);
      process2.setNumber(process.getNumber());

      when(repository.findByNumber(process.getNumber())).thenReturn(Optional.of(process));
      when(lawyerRepository.findByUsername(process2.getLawyer().getUsername()))
          .thenReturn(Optional.empty());

      assertThrows(LawyerNotFoundException.class, () -> service.update(process2.toDTO()));
    }
  }

  @Nested
  class Delete {

    @Test
    @DisplayName("Delete")
    void deleteShouldDeleteProcess() {

      when(repository.deleteByNumber(process.getNumber())).thenReturn(1L);

      service.delete(process.getNumber());

      verify(repository).deleteByNumber(process.getNumber());
    }
  }
}
