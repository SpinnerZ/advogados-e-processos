package online.publicacoes.avaliacaotecnica.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProcessFilterDTO {

  private final String username;
  private final Boolean archived;
}
