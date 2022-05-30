package online.publicacoes.avaliacaotecnica.resources;

import online.publicacoes.avaliacaotecnica.dto.ProcessDTO;
import online.publicacoes.avaliacaotecnica.dto.ProcessFilterDTO;
import online.publicacoes.avaliacaotecnica.services.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/process")
public class ProcessController {

  @Autowired private ProcessService service;

  @PostMapping
  public ResponseEntity<ProcessDTO> create(@RequestBody @Valid ProcessDTO dto) {

    ProcessDTO result = service.create(dto);
    URI uri =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{process}")
            .buildAndExpand(result.getNumber())
            .toUri();

    return ResponseEntity.created(uri).body(result);
  }

  @PostMapping("/mass")
  public ResponseEntity<List<ProcessDTO>> createAll(@RequestBody @Valid Set<ProcessDTO> dtos) {

    List<ProcessDTO> result = service.create(dtos);

    URI uri =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{process}")
            .buildAndExpand(result.stream().map(ProcessDTO::getNumber))
            .toUri();

    return ResponseEntity.created(uri).body(result);
  }

  @GetMapping
  public ResponseEntity<List<ProcessDTO>> retrieveAll() {

    return ResponseEntity.ok(service.retrieve());
  }

  @GetMapping("/number/{number}")
  public ResponseEntity<ProcessDTO> retrieveOne(
      @PathVariable @Positive(message = "Process number must be a positive number") Long number) {

    return ResponseEntity.of(service.retrieve(number));
  }

  @GetMapping("/filter")
  public ResponseEntity<List<ProcessDTO>> retrieveFiltered(
      @RequestParam(required = false) String username,
      @RequestParam(required = false) Boolean archived) {

    ProcessFilterDTO filter =
        ProcessFilterDTO.builder().username(username).archived(archived).build();

    return ResponseEntity.ok(service.retrieve(filter));
  }

  @PatchMapping("/{number}")
  public ResponseEntity<ProcessDTO> updateDefendant(
      @PathVariable @Positive(message = "Process number must be a positive number") Long number,
      @RequestParam String defendant) {

    return ResponseEntity.ok(service.update(number, defendant));
  }

  @PutMapping
  public ResponseEntity<ProcessDTO> update(@RequestBody @Valid ProcessDTO dto) {

    return ResponseEntity.ok(service.update(dto));
  }

  @DeleteMapping("/{number}")
  public ResponseEntity<Void> delete(@PathVariable @Positive Long number) {

    service.delete(number);

    return ResponseEntity.noContent().build();
  }
}
