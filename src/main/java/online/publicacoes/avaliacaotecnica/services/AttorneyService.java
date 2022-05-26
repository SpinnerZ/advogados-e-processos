package online.publicacoes.avaliacaotecnica.services;

import online.publicacoes.avaliacaotecnica.dto.AttorneyDTO;
import online.publicacoes.avaliacaotecnica.repositories.AttorneyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AttorneyService {

  @Autowired private AttorneyRepository repository;
  @Autowired private ProcessService processService;

  @Transactional
  public AttorneyDTO create(AttorneyDTO dto) {

    return null;
  }
}
