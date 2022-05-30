package online.publicacoes.avaliacaotecnica.exceptions.advice;

import online.publicacoes.avaliacaotecnica.exceptions.LawyerAlreadyExistsException;
import online.publicacoes.avaliacaotecnica.exceptions.LawyerNotFoundException;
import online.publicacoes.avaliacaotecnica.exceptions.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

@ControllerAdvice(basePackages = "online.publicacoes.avaliacaotecnica.resources")
public class LawyerControllerAdvice {

  @ResponseBody
  @ResponseStatus(HttpStatus.CONFLICT)
  @ExceptionHandler(LawyerAlreadyExistsException.class)
  public ErrorDTO handleLawyerAlreadyExists(LawyerAlreadyExistsException exception) {

    return ErrorDTO.builder()
        .status(HttpStatus.CONFLICT.value())
        .message("Username " + exception.getUsername() + " already exists")
        .dateTime(LocalDateTime.now())
        .build();
  }

  @ResponseBody
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(LawyerNotFoundException.class)
  public ErrorDTO handleLawyerNotFound(LawyerNotFoundException exception) {

    return ErrorDTO.builder()
        .status(HttpStatus.NOT_FOUND.value())
        .message("Username " + exception.getUsername() + " not found")
        .dateTime(LocalDateTime.now())
        .build();
  }
}
