package online.publicacoes.avaliacaotecnica.resources;

import online.publicacoes.avaliacaotecnica.dto.LawyerDTO;
import online.publicacoes.avaliacaotecnica.services.LawyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.constraints.NotBlank;
import java.net.URI;

@RestController
@RequestMapping(value = "/lawyer")
public class LawyerController {

  @Autowired LawyerService service;

  @PostMapping("/{username}")
  public ResponseEntity<LawyerDTO> create(
      @PathVariable @NotBlank(message = "Lawyer username cannot be blank") String username) {

    LawyerDTO result = service.create(username);
    URI uri =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{username}")
            .buildAndExpand(result.getUsername())
            .toUri();

    return ResponseEntity.created(uri).body(result);
  }

  @GetMapping("/{username}")
  public ResponseEntity<LawyerDTO> retrieve(
      @PathVariable @NotBlank(message = "Lawyer username cannot be blank") String username) {

    return ResponseEntity.of(service.retrieve(username));
  }

  @PatchMapping(value = "/{username}")
  public ResponseEntity<LawyerDTO> update(
      @PathVariable String username, @RequestParam(name = "new_username") String newUsername) {

    return ResponseEntity.ok(service.update(username, newUsername));
  }

  @DeleteMapping("/{username}")
  public ResponseEntity<Void> delete(
      @PathVariable @NotBlank(message = "Lawyer username cannot be blank") String username) {

    service.delete(username);

    return ResponseEntity.noContent().build();
  }
}
