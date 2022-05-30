package online.publicacoes.avaliacaotecnica.exceptions.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorDTO {

  private final int status;
  private final String message;
  private final LocalDateTime dateTime;
}
