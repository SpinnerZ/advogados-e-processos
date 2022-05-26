package online.publicacoes.avaliacaotecnica.services;

import online.publicacoes.avaliacaotecnica.dto.ProcessDTO;
import online.publicacoes.avaliacaotecnica.entities.Process;
import online.publicacoes.avaliacaotecnica.exceptions.NotAllProcessosSavedException;
import online.publicacoes.avaliacaotecnica.exceptions.ProcessAlreadyExistsException;
import online.publicacoes.avaliacaotecnica.repositories.ProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Service
public class ProcessService {

  @Autowired private ProcessRepository repository;

  @Transactional
  public ProcessDTO save(ProcessDTO processDTO) {

    if (doesProcessoAlreadyExists(processDTO)) {

      throw new ProcessAlreadyExistsException(processDTO);
    }

    Process process = repository.save(getProcessFromDTO(processDTO));

    return new ProcessDTO(process);
  }

  @Transactional
  public Set<ProcessDTO> save(Set<ProcessDTO> requestList) {

    Set<ProcessDTO> responseList = new HashSet<>();
    Set<ProcessDTO> errorsList = new HashSet<>();

    for (ProcessDTO processDTO : requestList) {

      try {

        responseList.add(save(processDTO));

      } catch (ProcessAlreadyExistsException e) {

        errorsList.add(processDTO);
      }
    }

    if (!errorsList.isEmpty()) {

      throw new NotAllProcessosSavedException(responseList, errorsList);
    }

    return responseList;
  }

  private boolean doesProcessoAlreadyExists(ProcessDTO processDTO) {

    return repository.existsByNumber(processDTO.getNumber());
  }

  private Process getProcessFromDTO(ProcessDTO processDTO) {

    return Process.builder()
        .archived(processDTO.getArchived())
        .number(processDTO.getNumber())
        .defendant(processDTO.getDefendant())
        .build();
  }
}
