package online.publicacoes.avaliacaotecnica.exceptions;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class ProcessNotFoundException extends RuntimeException {

  private final Long number;
}
