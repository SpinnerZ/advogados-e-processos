package online.publicacoes.avaliacaotecnica.services;

import online.publicacoes.avaliacaotecnica.dto.LawyerDTO;
import online.publicacoes.avaliacaotecnica.entities.Lawyer;
import online.publicacoes.avaliacaotecnica.exceptions.LawyerAlreadyExistsException;
import online.publicacoes.avaliacaotecnica.exceptions.LawyerNotFoundException;
import online.publicacoes.avaliacaotecnica.repositories.LawyerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@Service
public class LawyerService {

  @Autowired private LawyerRepository repository;
  @Autowired private ProcessService processService;

  @Transactional
  public LawyerDTO create(String username) {

    if (Boolean.TRUE.equals(repository.existsByUsername(username))) {

      throw new LawyerAlreadyExistsException(username);
    }

    Lawyer result =
        repository.save(Lawyer.builder().username(username).processes(new HashSet<>()).build());

    return new LawyerDTO(result);
  }

  @Transactional(readOnly = true)
  public LawyerDTO retrieve(String username) {

    Lawyer lawyer =
        repository
            .findByUsername(username)
            .orElseThrow(() -> new LawyerNotFoundException(username));

    return new LawyerDTO(lawyer);
  }

  @Transactional
  public LawyerDTO update(String oldUsername, String newUsername) {

    Lawyer lawyer =
        repository
            .findByUsername(oldUsername)
            .orElseThrow(() -> new LawyerNotFoundException(oldUsername));

    if (repository.findByUsername(newUsername).isPresent()) {
      throw new LawyerAlreadyExistsException(newUsername);
    }

    lawyer.setUsername(newUsername);
    lawyer = repository.save(lawyer);

    return new LawyerDTO(lawyer);
  }

  @Transactional
  public void delete(String username) {

    repository.deleteByUsername(username);
  }
}
