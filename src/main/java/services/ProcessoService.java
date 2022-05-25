package services;

import online.publicacoes.avaliacaotecnica.dto.ProcessoDTO;
import online.publicacoes.avaliacaotecnica.entities.Processo;
import online.publicacoes.avaliacaotecnica.exceptions.DuplicatedNumbersInRequestException;
import online.publicacoes.avaliacaotecnica.exceptions.NotAllProcessosSavedException;
import online.publicacoes.avaliacaotecnica.exceptions.ProcessoAlreadyExistsException;
import online.publicacoes.avaliacaotecnica.repositories.ProcessoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProcessoService {

  @Autowired private ProcessoRepository repository;

  @Transactional
  public ProcessoDTO save(ProcessoDTO processoDTO) {

    if (doesProcessoAlreadyExists(processoDTO)) {

      throw new ProcessoAlreadyExistsException(processoDTO.getNumero());
    }

    Processo processo = repository.save(getProcessoFromDTO(processoDTO));

    return new ProcessoDTO(processo);
  }

  @Transactional
  public List<ProcessoDTO> save(List<ProcessoDTO> requestList) {

    assertThatAllNumerosAreUnique(requestList);

    List<ProcessoDTO> responseList = new ArrayList<>();
    List<ProcessoDTO> errorsList = new ArrayList<>();

    for (ProcessoDTO processoDTO : requestList) {

      try {

        responseList.add(save(processoDTO));

      } catch (ProcessoAlreadyExistsException e) {

        errorsList.add(processoDTO);
      }
    }

    if (!errorsList.isEmpty()) {

      throw new NotAllProcessosSavedException(responseList, errorsList);
    }

    return responseList;
  }

  private void assertThatAllNumerosAreUnique(List<ProcessoDTO> requestList) {

    List<Integer> numerosList = requestList.stream().map(ProcessoDTO::getNumero).toList();
    Set<Integer> numerosSet = new HashSet<>();

    for (int numero : numerosList) {

      if (!numerosSet.add(numero)) {

        throw new DuplicatedNumbersInRequestException(requestList);
      }
    }
  }

  private boolean doesProcessoAlreadyExists(ProcessoDTO processoDTO) {

    return repository.existsByNumeroAndAtivoIsTrue(processoDTO.getNumero());
  }

  private Processo getProcessoFromDTO(ProcessoDTO processoDTO) {

    Processo processo = new Processo();
    processo.setAtivo(Boolean.TRUE);
    processo.setNumero(processoDTO.getNumero());
    processo.setReu(processoDTO.getReu());

    return processo;
  }
}
