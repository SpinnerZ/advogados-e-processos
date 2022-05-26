package online.publicacoes.avaliacaotecnica.services;

import online.publicacoes.avaliacaotecnica.dto.LawyerDTO;
import online.publicacoes.avaliacaotecnica.entities.Lawyer;
import online.publicacoes.avaliacaotecnica.exceptions.LawyerAlreadyExistsException;
import online.publicacoes.avaliacaotecnica.exceptions.LawyerNotFoundException;
import online.publicacoes.avaliacaotecnica.factories.LawyerFactory;
import online.publicacoes.avaliacaotecnica.factories.ProcessFactory;
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

    lawyer = LawyerFactory.getLawyer();

    when(repository.save(any())).then(returnsFirstArg());
  }

  @Nested
  @DisplayName("Save tests")
  class Save {

    @Test
    @DisplayName("Username is unique")
    void createShouldSaveLawyerWhenUsernameDoesNotExists() {

      when(repository.findByUsername(username)).thenReturn(Optional.empty());

      LawyerDTO result = service.create(username);

      assertEquals(username, result.getUsername());
      verify(repository).save(any());
    }

    @Test
    @DisplayName("Username already exists")
    void createShouldThrowLawyerAlreadyExistsExceptionWhenUsernameAlreadyExists() {

      when(repository.findByUsername(username)).thenReturn(Optional.of(lawyer));

      assertThrows(LawyerAlreadyExistsException.class, () -> service.create(username));
      verify(repository, never()).save(any());
    }
  }

  @Nested
  @DisplayName("Find tests")
  class Find {

    @Test
    @DisplayName("Username exists")
    void retrieveShouldReturnALawyerWhenItsUsernameIsFound() {

      lawyer.getProcesses().add(ProcessFactory.getProcess());

      when(repository.findByUsername(username)).thenReturn(Optional.of(lawyer));

      LawyerDTO result = service.retrieve(username);

      assertAll(
          () -> assertEquals(username, result.getUsername()),
          () -> assertEquals(1, result.getProcesses().size()));
    }

    @Test
    @DisplayName("Username does not exists")
    void retrieveShouldThrowLawyerNotFoundExceptionWhenUsernameIsNotFound() {

      when(repository.findByUsername(username)).thenReturn(Optional.empty());

      assertThrows(LawyerNotFoundException.class, () -> service.retrieve(username));
    }
  }

  @Nested
  @DisplayName("Update tests")
  class Update {

    String newUsername = "new.username";

    @Test
    @DisplayName("Old username exists and new username is unique")
    void updateShoudUpdateUsernameWhenUsernameExistsAndNewUsernameIsUnique() {

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
    void name() {}
  }
}
