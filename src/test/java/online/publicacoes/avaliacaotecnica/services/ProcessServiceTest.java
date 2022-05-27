package online.publicacoes.avaliacaotecnica.services;

import online.publicacoes.avaliacaotecnica.dto.ProcessDTO;
import online.publicacoes.avaliacaotecnica.entities.Process;
import online.publicacoes.avaliacaotecnica.exceptions.NotAllProcessosSavedException;
import online.publicacoes.avaliacaotecnica.exceptions.ProcessAlreadyExistsException;
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

import java.util.HashSet;
import java.util.Set;

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
class ProcessServiceTest {

  @InjectMocks ProcessService service;
  @Mock ProcessRepository repository;
  Process process;

  @BeforeEach
  void setUp() {

    process = ProcessFixture.getProcess();

    when(repository.save(any())).then(returnsFirstArg());
  }

  @Nested
  @DisplayName("Testes de criação")
  class SaveMethods {

    Set<ProcessDTO> processosRequestList;
    Process process2;

    @BeforeEach
    void setUp() {

      process2 = ProcessFixture.getProcess(2L);

      processosRequestList = new HashSet<>();
      processosRequestList.add(new ProcessDTO(process));
      processosRequestList.add(new ProcessDTO(process2));
    }

    @Test
    @DisplayName("Caminho feliz: Número do process não existe")
    void saveShouldSaveAProcessoWhenItIsUnique() {

      when(repository.existsByNumber(any())).thenReturn(false);

      ProcessDTO request = new ProcessDTO(process);
      ProcessDTO result = service.save(request);

      assertEquals(request, result, "O process retornado não foi igual ao enviado");
      verify(repository).existsByNumber(any());
      verify(repository).save(any());
    }

    @Test
    @DisplayName("Número do process já existe")
    void saveShouldProcessoAlreadyExistsExceptionWhenNumeroDoProcessoAlreadyExists() {

      when(repository.existsByNumber(any())).thenReturn(true);

      ProcessDTO request = new ProcessDTO(process);

      assertThrows(ProcessAlreadyExistsException.class, () -> service.save(request));

      verify(repository).existsByNumber(any());
      verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Caminho feliz: Nenhum process da lista existe")
    void saveShouldSaveAllProcessosWhenTheyAreUnique() {

      when(repository.existsByNumber(any())).thenReturn(false);

      Set<ProcessDTO> result = service.save(processosRequestList);

      assertEquals(processosRequestList, result);
      verify(repository, times(2)).existsByNumber(any());
      verify(repository, times(2)).save(any());
    }

    @Test
    @DisplayName("Quando algum dos processes da lista existe")
    void saveShouldThrowNotAllProcessosSavedExceptionWhenSomeProcessosAlreadyExists() {

      when(repository.existsByNumber(1L)).thenReturn(false);
      when(repository.existsByNumber(2L)).thenReturn(true);

      NotAllProcessosSavedException result =
          assertThrows(
              NotAllProcessosSavedException.class,
              (() -> service.save(processosRequestList)),
              "Não lançou exceção quando um item da lista já existia no repositório");

      verify(repository, times(2)).existsByNumber(any());
      verify(repository).save(any());

      assertAll(
          () -> assertEquals(1L, result.getSuccess().size()),
          () -> assertEquals(1L, result.getFailed().size()));
    }
  }
}
