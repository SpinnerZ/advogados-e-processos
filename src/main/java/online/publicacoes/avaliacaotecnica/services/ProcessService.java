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
import online.publicacoes.avaliacaotecnica.repositories.LawyerRepository;
import online.publicacoes.avaliacaotecnica.repositories.ProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ProcessService {

  @Autowired private ProcessRepository repository;
  @Autowired private LawyerRepository lawyerRepository;

  @Transactional
  public ProcessDTO create(ProcessDTO processDTO) {

    Lawyer lawyer =
        lawyerRepository
            .findByUsername(processDTO.getLawyerUsername())
            .orElseThrow(() -> new LawyerNotFoundException(processDTO.getLawyerUsername()));

    if (Boolean.TRUE.equals(repository.existsByNumber(processDTO.getNumber()))) {

      throw new ProcessAlreadyExistsException(processDTO.getNumber());
    }

    Process process =
        Process.builder()
            .lawyer(lawyer)
            .defendant(processDTO.getDefendant())
            .number(processDTO.getNumber())
            .archived(processDTO.getArchived())
            .build();

    return repository.save(process).toDTO();
  }

  @Transactional
  public List<ProcessDTO> create(Set<ProcessDTO> processDTOs) {

    List<ProcessDTO> success = new ArrayList<>();
    List<ProcessDTO> failure = new ArrayList<>();

    for (ProcessDTO processDTO : processDTOs) {

      try {

        ProcessDTO result = create(processDTO);
        success.add(result);

      } catch (Exception e) {

        failure.add(processDTO);
      }
    }

    if (failure.isEmpty()) {

      return success;
    }

    throw new NotAllProcessesCreatedException(success, failure);
  }

  @Transactional(readOnly = true)
  public List<ProcessDTO> retrieve() {

    return repository.findAll().stream().map(Process::toDTO).toList();
  }

  @Transactional(readOnly = true)
  public Optional<ProcessDTO> retrieve(Long number) {

    return repository.findByNumber(number).map(Process::toDTO);
  }

  @Transactional(readOnly = true)
  public List<ProcessDTO> retrieve(ProcessFilterDTO filter) {

    Lawyer lawyer = getLawyerFromFilter(filter);

    return repository.findAllByLawyerAndArchived(lawyer, filter.getArchived()).stream()
        .map(Process::toDTO)
        .toList();
  }

  private Lawyer getLawyerFromFilter(ProcessFilterDTO filter) {

    if (filter.getUsername() == null) {

      return null;
    }

    return lawyerRepository
        .findByUsername(filter.getUsername())
        .orElseThrow(() -> new LawyerNotFoundException(filter.getUsername()));
  }

  @Transactional
  public ProcessDTO update(Long number, String defendant) {

    Process process =
        repository.findByNumber(number).orElseThrow(() -> new ProcessNotFoundException(number));

    process.setDefendant(defendant);

    return repository.save(process).toDTO();
  }

  @Transactional
  public ProcessDTO update(ProcessDTO processDTO) {

    Process process =
        repository
            .findByNumber(processDTO.getNumber())
            .orElseThrow(() -> new ProcessCreatedInsteadOfUpdatedException(create(processDTO)));

    Lawyer lawyer =
        lawyerRepository
            .findByUsername(processDTO.getLawyerUsername())
            .orElseThrow(() -> new LawyerNotFoundException(processDTO.getLawyerUsername()));

    process = getNewProcess(process, processDTO, lawyer);

    return repository.save(process).toDTO();
  }

  private Process getNewProcess(Process process, ProcessDTO processDTO, Lawyer lawyer) {

    return Process.builder()
        .id(process.getId())
        .number(process.getNumber())
        .defendant(processDTO.getDefendant())
        .archived(processDTO.getArchived())
        .lawyer(lawyer)
        .build();
  }

  @Transactional
  public void delete(Long number) {

    repository.deleteByNumber(number);
  }
}
