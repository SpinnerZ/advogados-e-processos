package online.publicacoes.avaliacaotecnica.services;

import online.publicacoes.avaliacaotecnica.dto.ProcessoDTO;
import online.publicacoes.avaliacaotecnica.entities.Processo;
import online.publicacoes.avaliacaotecnica.exceptions.DuplicatedNumbersInRequestException;
import online.publicacoes.avaliacaotecnica.exceptions.NotAllProcessosSavedException;
import online.publicacoes.avaliacaotecnica.exceptions.ProcessoAlreadyExistsException;
import online.publicacoes.avaliacaotecnica.factories.ProcessoFactory;
import online.publicacoes.avaliacaotecnica.repositories.ProcessoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import services.ProcessoService;

import java.util.ArrayList;
import java.util.List;

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
class ProcessoServiceTest {

  @InjectMocks ProcessoService service;
  @Mock ProcessoRepository repository;
  Processo processo;

  @BeforeEach
  void setUp() {

    processo = ProcessoFactory.getProcesso();

    when(repository.save(any())).then(returnsFirstArg());
  }

  @Nested
  @DisplayName("Testes de criação")
  class SaveMethods {

    List<ProcessoDTO> processosRequestList;
    Processo processo2;

    @BeforeEach
    void setUp() {

      processo2 = ProcessoFactory.getProcesso(2);

      processosRequestList = new ArrayList<>();
      processosRequestList.add(new ProcessoDTO(processo));
      processosRequestList.add(new ProcessoDTO(processo2));
    }

    @Test
    @DisplayName("Caminho feliz: Número do processo não existe")
    void saveShouldSaveAProcessoWhenItIsUnique() {

      when(repository.existsByNumeroAndAtivoIsTrue(any())).thenReturn(false);

      ProcessoDTO request = new ProcessoDTO(processo);
      ProcessoDTO result = service.save(request);

      assertEquals(request, result, "O processo retornado não foi igual ao enviado");
      verify(repository).existsByNumeroAndAtivoIsTrue(any());
      verify(repository).save(any());
    }

    @Test
    @DisplayName("Número do processo já existe")
    void saveShouldProcessoAlreadyExistsExceptionWhenNumeroDoProcessoAlreadyExists() {

      when(repository.existsByNumeroAndAtivoIsTrue(any())).thenReturn(true);

      ProcessoDTO request = new ProcessoDTO(processo);

      assertThrows(ProcessoAlreadyExistsException.class, () -> service.save(request));

      verify(repository).existsByNumeroAndAtivoIsTrue(any());
      verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Caminho feliz: Nenhum processo da lista existe")
    void saveShouldSaveAllProcessosWhenTheyAreUnique() {

      when(repository.existsByNumeroAndAtivoIsTrue(any())).thenReturn(false);

      List<ProcessoDTO> result = service.save(processosRequestList);

      assertEquals(processosRequestList, result);
      verify(repository, times(2)).existsByNumeroAndAtivoIsTrue(any());
      verify(repository, times(2)).save(any());
    }

    @Test
    @DisplayName("Quando algum dos processos da lista existe")
    void saveShouldThrowNotAllProcessosSavedExceptionWhenSomeProcessosAlreadyExists() {

      when(repository.existsByNumeroAndAtivoIsTrue(1)).thenReturn(false);
      when(repository.existsByNumeroAndAtivoIsTrue(2)).thenReturn(true);

      NotAllProcessosSavedException result =
          assertThrows(
              NotAllProcessosSavedException.class,
              (() -> service.save(processosRequestList)),
              "Não lançou exceção quando um item da lista já existia no repositório");

      verify(repository, times(2)).existsByNumeroAndAtivoIsTrue(any());
      verify(repository).save(any());

      assertAll(
          () -> assertEquals(1, result.getSuccess().size()),
          () -> assertEquals(1, result.getFailed().size()));
    }

    @Test
    @DisplayName("Quando há processos duplicados na requisição")
    void
        saveShouldThrowDuplicatedNumbersInRequestExceptionWhenThereAreDuplicatedProcessosInRequest() {

      processosRequestList.get(1).setNumero(1);

      DuplicatedNumbersInRequestException result =
          assertThrows(
              DuplicatedNumbersInRequestException.class,
              (() -> service.save(processosRequestList)),
              "Não lançou exceção quando os itens da lista tem números iguais");

      verify(repository, never()).existsByNumeroAndAtivoIsTrue(any());
      verify(repository, never()).save(any());

      assertEquals(processosRequestList, result.getProcessos());
    }
  }
}
