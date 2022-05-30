package online.publicacoes.avaliacaotecnica.exceptions.advice;

import online.publicacoes.avaliacaotecnica.exceptions.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GeneralControllerAdvice {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorDTO> handleMethodArgumentNotValid(
      MethodArgumentNotValidException exception) {

    ErrorDTO errorDTO =
        ErrorDTO.builder()
            .status(HttpStatus.BAD_REQUEST.value())
            .message(exception.getMessage())
            .dateTime(LocalDateTime.now())
            .build();

    return ResponseEntity.badRequest().body(errorDTO);
  }
}
