package online.publicacoes.avaliacaotecnica.services;

import online.publicacoes.avaliacaotecnica.dto.LawyerDTO;
import online.publicacoes.avaliacaotecnica.entities.Lawyer;
import online.publicacoes.avaliacaotecnica.exceptions.LawyerAlreadyExistsException;
import online.publicacoes.avaliacaotecnica.repositories.LawyerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LawyerService {

  @Autowired private LawyerRepository repository;
  @Autowired private ProcessService processService;

  @Transactional
  public LawyerDTO create(String username) {

    if (repository.findByUsername(username).isPresent()) {

      throw new LawyerAlreadyExistsException(username);
    }

    Lawyer result = repository.save(Lawyer.builder().username(username).build());

    return new LawyerDTO(result);
  }

  @Transactional(readOnly = true)
  public LawyerDTO retrieve(String username) {

    return null;
  }

  @Transactional
  public LawyerDTO update(String oldUsername, String newUsername) {

    return null;
  }

  @Transactional
  public void delete(String username) {}
}
