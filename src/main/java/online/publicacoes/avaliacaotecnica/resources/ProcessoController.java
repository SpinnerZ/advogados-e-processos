package online.publicacoes.avaliacaotecnica.resources;

import online.publicacoes.avaliacaotecnica.entities.Processo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/processos")
public class ProcessoController {

  public ResponseEntity<Processo> findAll() {

    return ResponseEntity.ok(new Processo());
  }
}
