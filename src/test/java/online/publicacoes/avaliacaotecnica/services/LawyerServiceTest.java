package online.publicacoes.avaliacaotecnica.services;

import online.publicacoes.avaliacaotecnica.dto.LawyerDTO;
import online.publicacoes.avaliacaotecnica.entities.Lawyer;
import online.publicacoes.avaliacaotecnica.entities.Process;
import online.publicacoes.avaliacaotecnica.exceptions.LawyerAlreadyExistsException;
import online.publicacoes.avaliacaotecnica.exceptions.LawyerNotFoundException;
import online.publicacoes.avaliacaotecnica.fixturies.LawyerFixture;
import online.publicacoes.avaliacaotecnica.fixturies.ProcessFixture;
import online.publicacoes.avaliacaotecnica.repositories.LawyerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class LawyerServiceTest {

  @InjectMocks LawyerService service;
  @Mock LawyerRepository repository;

  Lawyer lawyer;
  String username = "user.name";

  @BeforeEach
  void setUp() {

    lawyer = LawyerFixture.getLawyer();

    when(repository.save(any())).then(returnsFirstArg());
  }

  @Nested
  class Save {

    @Test
    @DisplayName("Username is unique")
    void createShouldSaveLawyerWhenUsernameDoesNotExists() {

      when(repository.existsByUsername(username)).thenReturn(false);

      LawyerDTO result = service.create(username);

      assertEquals(username, result.getUsername());
      verify(repository).save(any());
    }

    @Test
    @DisplayName("Username already exists")
    void createShouldThrowLawyerAlreadyExistsExceptionWhenUsernameAlreadyExists() {

      when(repository.existsByUsername(username)).thenReturn(true);

      assertThrows(LawyerAlreadyExistsException.class, () -> service.create(username));
      verify(repository, never()).save(any());
    }
  }

  @Nested
  class Find {

    @Test
    @DisplayName("Username exists")
    void retrieveShouldReturnALawyerWhenItsUsernameIsFound() {

      Process process = ProcessFixture.getProcess();
      process.setLawyer(lawyer);

      lawyer.getProcesses().add(process);

      when(repository.findByUsername(username)).thenReturn(Optional.of(lawyer));

      Optional<LawyerDTO> result = service.retrieve(username);
      assertTrue(result.isPresent());

      LawyerDTO dto = result.get();

      assertAll(
          () -> assertEquals(username, dto.getUsername()),
          () -> assertEquals(1, dto.getProcesses().size()));
    }

    @Test
    @DisplayName("Username does not exists")
    void retrieveShouldReturnAnEmptyOptionalWhenUsernameIsNotFound() {

      when(repository.findByUsername(username)).thenReturn(Optional.empty());

      Optional<LawyerDTO> result = service.retrieve(username);
      assertTrue(result.isEmpty());
    }
  }

  @Nested
  class Update {

    String newUsername = "new.username";

    @Test
    @DisplayName("Old username exists and new username is unique")
    void updateShouldUpdateUsernameWhenUsernameExistsAndNewUsernameIsUnique() {

      when(repository.findByUsername(username)).thenReturn(Optional.of(lawyer));
      when(repository.findByUsername(newUsername)).thenReturn(Optional.empty());

      LawyerDTO result = service.update(username, newUsername);

      assertEquals(newUsername, result.getUsername());
      verify(repository, times(2)).findByUsername(any());
      verify(repository).save(any());
    }

    @Test
    @DisplayName("Old username exists and new username is not unique")
    void updateShouldThrowLawyerAlreadyExistsExceptionWhenNewUsernameAlreadyExists() {

      when(repository.findByUsername(any())).thenReturn(Optional.of(lawyer));

      LawyerAlreadyExistsException exception =
          assertThrows(
              LawyerAlreadyExistsException.class, (() -> service.update(username, newUsername)));
      assertEquals(newUsername, exception.getUsername());
      verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Old username does not exists")
    void updateShouldThrowLawyerNotFoundExceptionWhenLawyerDoesNotExists() {

      when(repository.findByUsername(username)).thenReturn(Optional.empty());

      LawyerNotFoundException exception =
          assertThrows(
              LawyerNotFoundException.class, (() -> service.update(username, newUsername)));
      assertEquals(username, exception.getUsername());
      verify(repository).findByUsername(any());
      verify(repository, never()).save(any());
    }
  }

  @Nested
  class Delete {

    @Test
    @DisplayName("Username exists")
    void deleteShouldDeleteLawyerWhenUsernameExists() {

      when(repository.deleteByUsername(username)).thenReturn(1L);

      service.delete(username);

      verify(repository).deleteByUsername(username);
    }
  }
}
