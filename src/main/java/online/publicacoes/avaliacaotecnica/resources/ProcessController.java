package online.publicacoes.avaliacaotecnica.resources;

import online.publicacoes.avaliacaotecnica.entities.Process;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/processes")
public class ProcessController {

  public ResponseEntity<Process> findAll() {

    return ResponseEntity.ok(new Process());
  }
}
