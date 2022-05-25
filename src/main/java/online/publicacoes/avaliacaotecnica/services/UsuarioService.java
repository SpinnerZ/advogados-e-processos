package online.publicacoes.avaliacaotecnica.services;

import online.publicacoes.avaliacaotecnica.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

  @Autowired private UsuarioRepository repository;
}
