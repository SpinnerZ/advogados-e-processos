package services;

import online.publicacoes.avaliacaotecnica.repositories.ProcessoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProcessoService {

  @Autowired private ProcessoRepository repository;
}
