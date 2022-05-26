package online.publicacoes.avaliacaotecnica.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class LawyerAlreadyExistsException extends RuntimeException {

  private final String username;
}
