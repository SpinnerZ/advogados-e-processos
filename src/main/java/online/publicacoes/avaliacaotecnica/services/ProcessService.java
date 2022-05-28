package online.publicacoes.avaliacaotecnica.services;

import online.publicacoes.avaliacaotecnica.dto.ProcessDTO;
import online.publicacoes.avaliacaotecnica.dto.ProcessFilterDTO;
import online.publicacoes.avaliacaotecnica.repositories.ProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ProcessService {

  @Autowired private ProcessRepository repository;
  @Autowired private LawyerService lawyerService;

  @Transactional
  public ProcessDTO create(ProcessDTO processDTO) {

    return null;
  }

  @Transactional
  public List<ProcessDTO> create(Set<ProcessDTO> processDTOs) {

    return null;
  }

  @Transactional(readOnly = true)
  public List<ProcessDTO> retrieve() {

    return null;
  }

  @Transactional(readOnly = true)
  public Optional<ProcessDTO> retrieve(Long number) {

    return null;
  }

  @Transactional(readOnly = true)
  public List<ProcessDTO> retrieve(ProcessFilterDTO filter) {

    return null;
  }

  @Transactional
  public ProcessDTO update(Long number, String defendant) {

    return null;
  }

  @Transactional
  public ProcessDTO update(ProcessDTO processDTO) {

    return null;
  }

  @Transactional
  public Void delete(Long number) {

    return null;
  }
}
