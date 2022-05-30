package online.publicacoes.avaliacaotecnica.exceptions.advice;

import online.publicacoes.avaliacaotecnica.dto.ProcessDTO;
import online.publicacoes.avaliacaotecnica.exceptions.NotAllProcessesCreatedException;
import online.publicacoes.avaliacaotecnica.exceptions.ProcessAlreadyExistsException;
import online.publicacoes.avaliacaotecnica.exceptions.ProcessCreatedInsteadOfUpdatedException;
import online.publicacoes.avaliacaotecnica.exceptions.ProcessNotFoundException;
import online.publicacoes.avaliacaotecnica.exceptions.dto.ErrorDTO;
import online.publicacoes.avaliacaotecnica.exceptions.dto.NotAllProcessesCreatedDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;

@ControllerAdvice(basePackages = "online.publicacoes.avaliacaotecnica.resources")
public class ProcessControllerAdvice {

  @ResponseBody
  @ResponseStatus(HttpStatus.CONFLICT)
  @ExceptionHandler(ProcessAlreadyExistsException.class)
  public ErrorDTO handleProcessAlreadyExists(ProcessAlreadyExistsException exception) {

    return ErrorDTO.builder()
        .status(HttpStatus.CONFLICT.value())
        .message("Process " + exception.getNumber() + " already exists")
        .dateTime(LocalDateTime.now())
        .build();
  }

  @ResponseBody
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(ProcessNotFoundException.class)
  public ErrorDTO handleProcessNotFound(ProcessNotFoundException exception) {

    return ErrorDTO.builder()
        .status(HttpStatus.NOT_FOUND.value())
        .message("Process " + exception.getNumber() + " not found")
        .dateTime(LocalDateTime.now())
        .build();
  }

  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(NotAllProcessesCreatedException.class)
  public NotAllProcessesCreatedDTO handleNotAllProcessesCreated(
      NotAllProcessesCreatedException exception) {

    return NotAllProcessesCreatedDTO.builder()
        .success(exception.getSuccess())
        .failure(exception.getFailure())
        .build();
  }

  @ExceptionHandler(ProcessCreatedInsteadOfUpdatedException.class)
  public ResponseEntity<ProcessDTO> handleProcessCreatedInsteadOfUpdated(
      ProcessCreatedInsteadOfUpdatedException exception) {

    URI uri =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{process}")
            .buildAndExpand(exception.getProcessDTO().getNumber())
            .toUri();

    return ResponseEntity.created(uri).body(exception.getProcessDTO());
  }
}
