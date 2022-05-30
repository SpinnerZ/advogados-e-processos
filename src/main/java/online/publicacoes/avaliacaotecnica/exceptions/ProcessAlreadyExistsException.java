package online.publicacoes.avaliacaotecnica.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ProcessAlreadyExistsException extends RuntimeException {

  private final Long number;
}
